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
import java.util.List;
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

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("username", username);
        form.add("password", password);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                tokenEndpoint, new HttpEntity<>(form, headers), Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("access_token"))
                throw new Exception("No se pudo obtener el token de acceso.");
            return processToken((String) body.get("access_token"));
        } catch (HttpClientErrorException e) {
            throw new Exception("Credenciales incorrectas.");
        }
    }

    private AuthResult processToken(String accessToken) throws Exception {
        String[] parts = accessToken.split("\\.");
        if (parts.length < 2) throw new Exception("Token JWT inválido.");

        Map<String, Object> claims = objectMapper.readValue(
            new String(Base64.getUrlDecoder().decode(parts[1])),
            new TypeReference<Map<String, Object>>() {});

        Long dbId = extractLong(claims, "db_id");
        String tipoPersona = extractString(claims, "tipo_persona");

        // Fallback: buscar por preferred_username si no hay claims personalizados
        if (dbId == null || tipoPersona == null) {
            String username = (String) claims.get("preferred_username");
            if (username == null)
                throw new Exception("No se pudo identificar al usuario desde el token.");

            var optEst = estudianteRepository.findByDni(username);
            if (optEst.isPresent()) {
                dbId = optEst.get().getIdEstudiante();
                tipoPersona = "ESTUDIANTE";
            } else {
                var optAdmin = administradorRepository.findByUsuario(username);
                if (optAdmin.isPresent()) {
                    dbId = optAdmin.get().getIdAdmin();
                    tipoPersona = "ADMIN";
                } else {
                    throw new Exception("Usuario autenticado en Keycloak pero no existe en la BD: " + username);
                }
            }
        }

        if ("ADMIN".equalsIgnoreCase(tipoPersona)) {
            final Long id = dbId;
            Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new Exception("Administrador no encontrado: " + id));
            return new AuthResult(admin, "ADMIN", accessToken);
        } else if ("ESTUDIANTE".equalsIgnoreCase(tipoPersona)) {
            final Long id = dbId;
            Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new Exception("Estudiante no encontrado: " + id));
            return new AuthResult(estudiante, "ESTUDIANTE", accessToken);
        } else {
            throw new Exception("Tipo de persona desconocido: " + tipoPersona);
        }
    }

    private Long extractLong(Map<String, Object> claims, String key) {
        Object val = claims.get(key);
        if (val instanceof Number) return ((Number) val).longValue();
        if (val instanceof String) return Long.parseLong((String) val);
        if (val instanceof List<?> list && !list.isEmpty()) return Long.parseLong(list.get(0).toString());
        return null;
    }

    private String extractString(Map<String, Object> claims, String key) {
        Object val = claims.get(key);
        if (val instanceof String) return (String) val;
        if (val instanceof List<?> list && !list.isEmpty()) return list.get(0).toString();
        return null;
    }
}
