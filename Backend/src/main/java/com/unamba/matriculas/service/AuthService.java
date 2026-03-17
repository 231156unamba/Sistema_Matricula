package com.unamba.matriculas.service;

import com.unamba.matriculas.dto.AuthResult;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de autenticación para estudiantes ingresantes y regulares.
 * 
 * FORMATO DEL CÓDIGO DE ESTUDIANTE:
 * El código tiene 6 dígitos: AASNNNN
 * - AA: Últimos 2 dígitos del año de ingreso (ej: 23 para 2023)
 * - S: Semestre de ingreso (1 = primera mitad del año, 2 = segunda mitad del año)
 * - NNNN: Número correlativo del estudiante
 * 
 * Ejemplos:
 * - 231156: Ingresó en el año 2023, semestre 2023-1 (primera mitad), número 1156
 * - 222115: Ingresó en el año 2022, semestre 2022-2 (segunda mitad), número 2115
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    // Devolvemos el manejo de tokens al frontend si es necesario,
    // o simplemente mantenemos la estructura que devuelve el Estudiante.
    private final EstudianteRepository estudianteRepository;
    private final KeycloakIntegrationService keycloakIntegrationService;
    
    public AuthResult loginIngresante(String dni, String voucher) throws Exception {
        // En keycloak, el username para la bd es el dni.
        // La contraseña para ingresantes, tal como se indicó en tus reglas, es el voucher.
        AuthResult authResult = keycloakIntegrationService.login(dni, voucher);
        
        if ("ESTUDIANTE".equals(authResult.getTipoPersona()) && authResult.getUser() instanceof Estudiante) {
            Estudiante estudiante = (Estudiante) authResult.getUser();
            if (Estudiante.TipoEstudiante.INGRESANTE != estudiante.getTipo()) {
                throw new Exception("El usuario autenticado no es un INGRESANTE.");
            }
            return authResult;
        } else {
            throw new Exception("Tipo de persona incorrecto para este login.");
        }
    }
    
    public AuthResult loginRegular(String dni, String codigoEstudiante) throws Exception {
        // Para estudiantes Regulares, el username es el dni, el password es el código de estudiante.
        AuthResult authResult = keycloakIntegrationService.login(dni, codigoEstudiante);
        
        if ("ESTUDIANTE".equals(authResult.getTipoPersona()) && authResult.getUser() instanceof Estudiante) {
            Estudiante estudiante = (Estudiante) authResult.getUser();
            
            if (estudiante.getEstado() != Estudiante.EstadoEstudiante.ACTIVO) {
                throw new Exception("Estudiante no está activo en el sistema local");
            }
            
            if (Estudiante.TipoEstudiante.REGULAR != estudiante.getTipo()) {
                throw new Exception("El usuario autenticado no es un REGULAR.");
            }
            
            return authResult;
        } else {
            throw new Exception("Tipo de persona incorrecto.");
        }
    }
    
    public AuthResult loginAdmin(String usuario, String password) throws Exception {
        AuthResult authResult = keycloakIntegrationService.login(usuario, password);
        
        if ("ADMIN".equals(authResult.getTipoPersona()) && authResult.getUser() instanceof Administrador) {
            Administrador admin = (Administrador) authResult.getUser();
            if (!admin.getActivo()) {
                throw new Exception("El administrador no está activo.");
            }
            return authResult;
        } else {
            throw new Exception("El usuario autenticado no tiene permisos de administrador.");
        }
    }
}
