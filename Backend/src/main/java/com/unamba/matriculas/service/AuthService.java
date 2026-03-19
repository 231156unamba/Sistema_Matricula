package com.unamba.matriculas.service;

import com.unamba.matriculas.dto.AuthResult;
import com.unamba.matriculas.model.Administrador;
import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.model.Pago;
import com.unamba.matriculas.repository.EstudianteRepository;
import com.unamba.matriculas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

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
        // Buscamos al estudiante por DNI
        Estudiante estudiante = estudianteRepository.findByDni(dni)
            .orElseThrow(() -> new Exception("Estudiante no encontrado con el DNI proporcionado."));

        if (estudiante.getTipo() != Estudiante.TipoEstudiante.REGULAR) {
            throw new Exception("El estudiante no es de tipo REGULAR.");
        }

        if (estudiante.getEstado() != Estudiante.EstadoEstudiante.ACTIVO) {
            throw new Exception("El estudiante no está activo.");
        }

        // DEBUG LOGS
        String voucherTrimmed = voucher.trim();
        System.out.println("Validando voucher para DNI: " + dni + " y Voucher (original): '" + voucher + "', (trimmed): '" + voucherTrimmed + "'");

        // Validamos el pago de matrícula con el voucher (Búsqueda flexible)
        Optional<Pago> pagoGeneralOpt = pagoRepository.findByVoucherIgnoreCase(voucherTrimmed);
        
        if (pagoGeneralOpt.isEmpty()) {
            System.out.println("Error: El voucher '" + voucherTrimmed + "' NO EXISTE en la tabla pagos (búsqueda insensitiva).");
            
            // DIAGNÓSTICO TOTAL: ¿Qué hay en la tabla pagos?
            List<Pago> todos = pagoRepository.findAll();
            System.out.println("DEBUG: Hay " + todos.size() + " registros en la tabla 'pagos'.");
            for (Pago p : todos) {
                System.out.println(" - ID: " + p.getIdPago() + ", Voucher: '" + p.getVoucher() + "', DNI: " + (p.getEstudiante() != null ? p.getEstudiante().getDni() : "null"));
            }
            
            throw new Exception("El número de voucher ingresado no existe en nuestra base de datos.");
        }

        Pago pago = pagoGeneralOpt.get();
        System.out.println("Voucher encontrado: ID=" + pago.getIdPago() + 
                           ", EstudianteID=" + (pago.getEstudiante() != null ? pago.getEstudiante().getIdEstudiante() : "NULL") + 
                           ", Tipo=" + pago.getTipoPago() + 
                           ", Validado=" + pago.getValidado());

        if (Boolean.FALSE.equals(pago.getValidado())) {
            System.out.println("Error: El voucher existe pero NO está validado (validado=0).");
            throw new Exception("El voucher de pago aún no ha sido validado por el área de tesorería.");
        }

        if (pago.getEstudiante() == null || !pago.getEstudiante().getIdEstudiante().equals(estudiante.getIdEstudiante())) {
            String dueñoID = (pago.getEstudiante() != null) ? pago.getEstudiante().getIdEstudiante().toString() : "nadie";
            System.out.println("Error: El voucher pertenece a " + dueñoID + ", pero intentó usarlo " + estudiante.getIdEstudiante());
            throw new Exception("El voucher proporcionado pertenece a otro estudiante.");
        }

        if (!Pago.TipoPago.MATRICULA.equals(pago.getTipoPago())) {
            System.out.println("Error: El voucher no es de tipo MATRICULA. Tipo actual: " + pago.getTipoPago());
            throw new Exception("El voucher proporcionado no es un pago de matrícula.");
        }

        // Generamos un token real de Keycloak para que el frontend pueda llamar a otros endpoints (como cursos)
        // El username es el DNI y el password para alumnos regulares es su código de estudiante.
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
