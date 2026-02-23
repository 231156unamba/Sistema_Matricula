package com.unamba.matriculas.service;

import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteService {
    
    private final EstudianteRepository estudianteRepository;
    
    public List<Estudiante> obtenerTodos() {
        return estudianteRepository.findAll();
    }
    
    public Estudiante obtenerPorId(Long id) throws Exception {
        return estudianteRepository.findById(id)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
    }
    
    public Estudiante obtenerPorCodigo(String codigo) throws Exception {
        return estudianteRepository.findByCodigoEstudiante(codigo)
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
    }
    
    public Estudiante actualizarEstado(Long id, Estudiante.EstadoEstudiante nuevoEstado) throws Exception {
        Estudiante estudiante = obtenerPorId(id);
        estudiante.setEstado(nuevoEstado);
        return estudianteRepository.save(estudiante);
    }
    
    public Estudiante actualizarCreditos(Long id, Integer nuevosCreditos) throws Exception {
        Estudiante estudiante = obtenerPorId(id);
        estudiante.setCreditosMaximos(nuevosCreditos);
        return estudianteRepository.save(estudiante);
    }
}
