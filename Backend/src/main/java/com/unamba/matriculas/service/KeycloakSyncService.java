package com.unamba.matriculas.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.model.Pago;
import com.unamba.matriculas.repository.AdministradorRepository;
import com.unamba.matriculas.repository.EstudianteRepository;
import com.unamba.matriculas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakSyncService {

    private final AdministradorRepository administradorRepository;
    private final EstudianteRepository estudianteRepository;
    private final PagoRepository pagoRepository;

    @Value("${keycloak.auth-server-url:http://localhost:8180}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:Matricula}")
    private String realm;

    @Value("${keycloak.resource:app-matricula}")
    private String clientId;

    @Value("${keycloak.credentials.secret:L4itKVl3Wpw029O6BEnWTRnkHgghqaWK}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String syncAllUsers() {
        try {
            String token = getAdminToken();
            int adminsSynced = syncAdministradores(token);
            int estudiantesSynced = syncEstudiantes(token);
            return "Sincronización completada. Administradores: " + adminsSynced + ", Estudiantes: " + estudiantesSynced;
        } catch (Exception e) {
            log.error("Error sincronizando usuarios a Keycloak", e);
            return "Error en sincronización: " + e.getMessage();
        }
    }

    private String getAdminToken() throws Exception {
        String tokenEndpoint = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("access_token")) {
                return (String) body.get("access_token");
            }
            throw new Exception("No se pudo obtener el token de administrador.");
        } catch (HttpClientErrorException e) {
            throw new Exception("Error obteniendo token admin de Keycloak: " + e.getResponseBodyAsString());
        }
    }

    private int syncAdministradores(String token) {
        List<Administrador> admins = administradorRepository.findAll();
        int count = 0;
        for (Administrador admin : admins) {
            try {
                Map<String, Object> userPayload = buildBaseUserPayload(
                        admin.getUsuario(),
                        admin.getPassword(),
                        admin.getNombres(),
                        admin.getApellidos(),
                        admin.getEmail()
                );

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("db_id", Collections.singletonList(String.valueOf(admin.getIdAdmin())));
                attributes.put("tipo_persona", Collections.singletonList("ADMIN"));
                attributes.put("sub_tipo", Collections.singletonList(admin.getRol().name()));
                attributes.put("activo", Collections.singletonList(String.valueOf(admin.getActivo())));
                userPayload.put("attributes", attributes);

                createUserInKeycloak(token, userPayload);
                count++;
            } catch (Exception e) {
                log.warn("Fallo al sincronizar admin {}", admin.getUsuario(), e);
            }
        }
        return count;
    }

    private int syncEstudiantes(String token) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        int count = 0;
        for (Estudiante est : estudiantes) {
            try {
                String password = resolveEstudiantePassword(est);
                if (password == null) {
                    log.warn("Estudiante {} no tiene contraseña válida, saltando.", est.getDni());
                    continue;
                }

                String email = est.getCodigoEstudiante() != null ? est.getCodigoEstudiante() + "@unamba.edu.pe" : est.getDni() + "@unamba.edu.pe";

                Map<String, Object> userPayload = buildBaseUserPayload(
                        est.getDni(), 
                        password,
                        est.getNombres(),
                        est.getApellidos(),
                        email
                );

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("db_id", Collections.singletonList(String.valueOf(est.getIdEstudiante())));
                attributes.put("tipo_persona", Collections.singletonList("ESTUDIANTE"));
                attributes.put("sub_tipo", Collections.singletonList(est.getTipo().name()));
                attributes.put("dni", Collections.singletonList(est.getDni()));
                attributes.put("activo", Collections.singletonList(String.valueOf(est.getEstado() == Estudiante.EstadoEstudiante.ACTIVO)));
                userPayload.put("attributes", attributes);

                createUserInKeycloak(token, userPayload);
                count++;
            } catch (Exception e) {
                log.warn("Fallo al sincronizar estudiante {}", est.getDni(), e);
            }
        }
        return count;
    }

    private String resolveEstudiantePassword(Estudiante estudiante) {
        if (Estudiante.TipoEstudiante.REGULAR == estudiante.getTipo()) {
            return estudiante.getCodigoEstudiante();
        } else if (Estudiante.TipoEstudiante.INGRESANTE == estudiante.getTipo()) {
            return pagoRepository.findFirstByEstudiante_IdEstudianteOrderByFechaPagoDesc(estudiante.getIdEstudiante())
                    .map(Pago::getVoucher)
                    .orElse(null);
        }
        return null;
    }

    private Map<String, Object> buildBaseUserPayload(String username, String password, String firstName, String lastName, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("enabled", true);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        if (email != null) user.put("email", email);
        
        Map<String, Object> cred = new HashMap<>();
        cred.put("type", "password");
        cred.put("value", password);
        cred.put("temporary", false);
        
        user.put("credentials", Collections.singletonList(cred));
        return user;
    }

    private void createUserInKeycloak(String token, Map<String, Object> userPayload) throws Exception {
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful() && response.getStatusCode() != HttpStatus.CONFLICT) {
                throw new Exception("Respuesta status " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.info("Usuario {} ya existe en Keycloak.", userPayload.get("username"));
            } else {
                throw new Exception("Error API Keycloak: " + e.getResponseBodyAsString());
            }
        }
    }
}
