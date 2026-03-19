package com.unamba.matriculas.service;

import com.unamba.matriculas.model.Estudiante;
import com.unamba.matriculas.model.DetalleMatricula;
import com.unamba.matriculas.model.Pago;
import com.unamba.matriculas.repository.EstudianteRepository;
import com.unamba.matriculas.repository.DetalleMatriculaRepository;
import com.unamba.matriculas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstudianteService {
    
    private final EstudianteRepository estudianteRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final PagoRepository pagoRepository;
    
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
    
    public Optional<Estudiante> buscarPorDni(String dni) {
        return estudianteRepository.findByDni(dni);
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
    
    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }
    
    public void eliminar(Long id) throws Exception {
        Estudiante estudiante = obtenerPorId(id);
        estudianteRepository.delete(estudiante);
    }
    
    public List<Estudiante> obtenerPorTipo(Estudiante.TipoEstudiante tipo) {
        return estudianteRepository.findByTipo(tipo);
    }
    
    public List<Estudiante> obtenerPorEstado(Estudiante.EstadoEstudiante estado) {
        return estudianteRepository.findByEstado(estado);
    }
    
    public long contarPorTipo(Estudiante.TipoEstudiante tipo) {
        return estudianteRepository.countByTipo(tipo);
    }

    public boolean verificarPagoMatricula(Long idEstudiante) {
        List<Pago> pagosValidados = pagoRepository.findByEstudiante_IdEstudianteAndTipoPagoAndValidadoTrue(
            idEstudiante, Pago.TipoPago.MATRICULA
        );
        return !pagosValidados.isEmpty();
    }

    public Map<String, Object> obtenerResumenAcademico(Long idEstudiante) throws Exception {
        Estudiante estudiante = obtenerPorId(idEstudiante);

        List<DetalleMatricula> aprobados = detalleMatriculaRepository.findCursosAprobadosByEstudiante(idEstudiante);
        long cursosAprobados = aprobados.size();
        int creditosAprobados = aprobados.stream()
            .map(DetalleMatricula::getCurso)
            .filter(c -> c != null && c.getCreditos() != null)
            .mapToInt(c -> c.getCreditos())
            .sum();

        return Map.of(
            "idEstudiante", estudiante.getIdEstudiante(),
            "cursosAprobados", cursosAprobados,
            "creditosAprobados", creditosAprobados,
            "creditosMaximos", estudiante.getCreditosMaximos()
        );
    }
}
