package com.unamba.matriculas.service;

import com.unamba.matriculas.model.Anuncio;
import com.unamba.matriculas.repository.AnuncioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnuncioService {
    
    private final AnuncioRepository anuncioRepository;
    
    public List<Anuncio> obtenerActivos() {
        return anuncioRepository.findByActivoTrueOrderByFechaCreacionDesc();
    }
    
    public List<Anuncio> obtenerTodos() {
        return anuncioRepository.findAll();
    }
    
    public Anuncio crear(Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }
    
    public Anuncio actualizar(Long id, Anuncio anuncio) throws Exception {
        Anuncio existente = anuncioRepository.findById(id)
            .orElseThrow(() -> new Exception("Anuncio no encontrado"));
        
        existente.setTitulo(anuncio.getTitulo());
        existente.setContenido(anuncio.getContenido());
        existente.setTipo(anuncio.getTipo());
        existente.setFechaInicio(anuncio.getFechaInicio());
        existente.setFechaFin(anuncio.getFechaFin());
        existente.setActivo(anuncio.getActivo());
        
        return anuncioRepository.save(existente);
    }
    
    public void eliminar(Long id) {
        anuncioRepository.deleteById(id);
    }
}
