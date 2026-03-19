package com.unamba.matriculas.service;

import com.unamba.matriculas.dto.AuthResult;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.model.Pago;
import com.unamba.matriculas.repository.EstudianteRepository;
import com.unamba.matriculas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    // Devolvemos el manejo de tokens al frontend si es necesario,
    // o simplemente mantenemos la estructura que devuelve el Estudiante.
    private final EstudianteRepository estudianteRepository;
    private final KeycloakIntegrationService keycloakIntegrationService;
    private final PagoRepository pagoRepository;
    
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

    public AuthResult loginMatriculaRegular(String dni, String voucher) throws Exception {
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new Exception("Estudiante no encontrado con el DNI proporcionado."));

        if (estudiante.getTipo() != Estudiante.TipoEstudiante.REGULAR)
            throw new Exception("El estudiante no es de tipo REGULAR.");

        if (estudiante.getEstado() != Estudiante.EstadoEstudiante.ACTIVO)
            throw new Exception("El estudiante no está activo.");

        Pago pago = pagoRepository.findByVoucherIgnoreCase(voucher.trim())
            .orElseThrow(() -> new Exception("El número de voucher ingresado no existe en nuestra base de datos."));

        if (Boolean.FALSE.equals(pago.getValidado()))
            throw new Exception("El voucher de pago aún no ha sido validado por el área de tesorería.");

        if (pago.getEstudiante() == null || !pago.getEstudiante().getIdEstudiante().equals(estudiante.getIdEstudiante()))
            throw new Exception("El voucher proporcionado pertenece a otro estudiante.");

        if (!Pago.TipoPago.MATRICULA.equals(pago.getTipoPago()))
            throw new Exception("El voucher proporcionado no es un pago de matrícula.");

        return keycloakIntegrationService.login(dni, estudiante.getCodigoEstudiante());
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
