package com.unamba.matriculas.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unamba.matriculas.dto.AuthResult;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.repository.AdministradorRepository;
import com.unamba.matriculas.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakIntegrationService {

    private final EstudianteRepository estudianteRepository;
    private final AdministradorRepository administradorRepository;

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

    public AuthResult login(String username, String password) throws Exception {
        String tokenEndpoint = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("access_token")) {
                String accessToken = (String) body.get("access_token");
                return processToken(accessToken);
            } else {
                throw new Exception("No se pudo obtener el token de acceso.");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Credenciales incorrectas o error en Keycloak: " + e.getStatusCode());
        }
    }

    private AuthResult processToken(String accessToken) throws Exception {
        String[] parts = accessToken.split("\\.");
        if (parts.length < 2) {
            throw new Exception("Token JWT inválido formatado.");
        }

        String payloadBase64 = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payloadBase64));

        Map<String, Object> claims = objectMapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});

        Long dbId = null;
        String tipoPersona = null;
        
        if (claims.containsKey("db_id")) {
            Object idObj = claims.get("db_id");
            if (idObj instanceof Number) {
                dbId = ((Number) idObj).longValue();
            } else if (idObj instanceof String) {
                dbId = Long.parseLong((String) idObj);
            } else if (idObj instanceof java.util.List) {
                dbId = Long.parseLong(((java.util.List<String>) idObj).get(0));
            }
        }
        
        if (claims.containsKey("tipo_persona")) {
            Object tipoObj = claims.get("tipo_persona");
            if (tipoObj instanceof String) {
                tipoPersona = (String) tipoObj;
            } else if (tipoObj instanceof java.util.List) {
                tipoPersona = ((java.util.List<String>) tipoObj).get(0);
            }
        }

        if (dbId == null || tipoPersona == null) {
            String usernameToFind = (String) claims.get("preferred_username");
            if (usernameToFind == null) {
                throw new Exception("Faltan claims en Keycloak y no se pudo obtener el preferred_username. Claims actuales: " + claims.keySet());
            }

            java.util.Optional<Estudiante> optEst = estudianteRepository.findByDni(usernameToFind);
            if (optEst.isPresent()) {
                dbId = optEst.get().getIdEstudiante();
                tipoPersona = "ESTUDIANTE";
            } else {
                java.util.Optional<Administrador> optAdmin = administradorRepository.findByUsuario(usernameToFind);
                if (optAdmin.isPresent()) {
                    dbId = optAdmin.get().getIdAdmin();
                    tipoPersona = "ADMIN";
                } else {
                    throw new Exception("Usuario autenticado en Keycloak pero no existe en BD local: " + usernameToFind);
                }
            }
        }
        
        // El claim "activo" también puede ser validado aquí
        if (claims.containsKey("activo")) {
            Boolean activo = (Boolean) claims.get("activo");
            if (activo != null && !activo) {
                throw new Exception("El usuario está inactivo en Keycloak.");
            }
        }

        if ("ADMIN".equalsIgnoreCase(tipoPersona)) {
            final Long finalDbId = dbId;
            Administrador admin = administradorRepository.findById(dbId)
                    .orElseThrow(() -> new Exception("Administrador no encontrado en la BD local con ID: " + finalDbId));
            return new AuthResult(admin, "ADMIN", accessToken);
            
        } else if ("ESTUDIANTE".equalsIgnoreCase(tipoPersona)) {
            final Long finalDbId = dbId;
            Estudiante estudiante = estudianteRepository.findById(dbId)
                    .orElseThrow(() -> new Exception("Estudiante no encontrado en la BD local con ID: " + finalDbId));
            return new AuthResult(estudiante, "ESTUDIANTE", accessToken);
        } else {
            throw new Exception("tipo_persona desconocido: " + tipoPersona);
        }
    }
}
