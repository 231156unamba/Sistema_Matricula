package com.unamba.matriculas.controller;

import com.unamba.matriculas.service.KeycloakSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSyncController {

    private final KeycloakSyncService keycloakSyncService;

    @PostMapping("/sync-keycloak")
    public ResponseEntity<String> syncUsersToKeycloak() {
        String result = keycloakSyncService.syncAllUsers();
        return ResponseEntity.ok(result);
    }
}
