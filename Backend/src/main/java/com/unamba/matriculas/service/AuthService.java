package com.unamba.matriculas.service;

import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.model.Pago;
import com.unamba.matriculas.repository.EstudianteRepository;
import com.unamba.matriculas.repository.PagoRepository;
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
    
    private final EstudianteRepository estudianteRepository;
    private final PagoRepository pagoRepository;
    
    public Estudiante loginIngresante(String dni, String voucher) throws Exception {
        Optional<Pago> pago = pagoRepository.findByVoucherAndValidadoTrue(voucher);
        if (pago.isEmpty()) {
            throw new Exception("Voucher no válido");
        }
        
        return estudianteRepository.findByDni(dni).orElse(null);
    }
    
    public Estudiante loginRegular(String codigoEstudiante, String voucher) throws Exception {
        Optional<Pago> pago = pagoRepository.findByVoucherAndValidadoTrue(voucher);
        if (pago.isEmpty()) {
            throw new Exception("Voucher no válido");
        }
        
        Estudiante estudiante = estudianteRepository.findByCodigoEstudiante(codigoEstudiante)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        
        if (estudiante.getEstado() != Estudiante.EstadoEstudiante.ACTIVO) {
            throw new Exception("Estudiante no está activo");
        }
        
        return estudiante;
    }
}
