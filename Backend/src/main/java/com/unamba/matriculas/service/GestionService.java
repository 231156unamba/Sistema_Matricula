package com.unamba.matriculas.service;

import com.unamba.matriculas.model.*;
import com.unamba.matriculas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionService {
    
    private final PagoRepository pagoRepository;
    private final DocumentoIngresanteRepository documentoRepository;
    private final EstudianteRepository estudianteRepository;
    private final AnuncioRepository anuncioRepository;
    private final AdministradorRepository administradorRepository;
    
    // Gestión de Pagos
    public List<Pago> obtenerTodosPagos() {
        return pagoRepository.findAll();
    }
    
    public List<Pago> obtenerPagosPorEstudiante(Long idEstudiante) {
        return pagoRepository.findByEstudiante_IdEstudiante(idEstudiante);
    }
    
    public List<Pago> obtenerPagosPendientesValidacion() {
        return pagoRepository.findByValidadoFalse();
    }
    
    public List<Pago> obtenerPagosPorTipo(Pago.TipoPago tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago);
    }
    
    @Transactional
    public Pago registrarPago(Pago pago) throws Exception {
        // Validar que el estudiante exista
        Estudiante estudiante = estudianteRepository.findById(pago.getEstudiante().getIdEstudiante())
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        
        // Validar que no exista un pago con el mismo voucher
        Optional<Pago> pagoExistente = pagoRepository.findByVoucherAndValidadoTrue(pago.getVoucher());
        if (pagoExistente.isPresent()) {
            throw new Exception("Ya existe un pago validado con este voucher");
        }
        
        pago.setEstudiante(estudiante);
        pago.setValidado(false);
        pago.setFechaPago(LocalDate.now());
        
        return pagoRepository.save(pago);
    }
    
    @Transactional
    public Pago validarPago(Long idPago) throws Exception {
        Pago pago = pagoRepository.findById(idPago)
            .orElseThrow(() -> new Exception("Pago no encontrado"));
        
        if (pago.getValidado()) {
            throw new Exception("El pago ya está validado");
        }
        
        pago.setValidado(true);
        return pagoRepository.save(pago);
    }
    
    @Transactional
    public void rechazarPago(Long idPago) throws Exception {
        Pago pago = pagoRepository.findById(idPago)
            .orElseThrow(() -> new Exception("Pago no encontrado"));
        
        if (pago.getValidado()) {
            throw new Exception("No se puede rechazar un pago ya validado");
        }
        
        pagoRepository.delete(pago);
    }
    
    // Gestión de Documentos de Ingresantes
    public List<DocumentoIngresante> obtenerTodosDocumentos() {
        return documentoRepository.findAll();
    }
    
    public Optional<DocumentoIngresante> obtenerDocumentoPorEstudiante(Long idEstudiante) {
        return documentoRepository.findByEstudiante_IdEstudiante(idEstudiante);
    }
    
    @Transactional
    public DocumentoIngresante guardarDocumento(DocumentoIngresante documento) throws Exception {
        Estudiante estudiante = estudianteRepository.findById(documento.getEstudiante().getIdEstudiante())
            .orElseThrow(() -> new Exception("Estudiante no encontrado"));
        
        if (estudiante.getTipo() != Estudiante.TipoEstudiante.INGRESANTE) {
            throw new Exception("Los documentos solo aplican para estudiantes ingresantes");
        }
        
        documento.setEstudiante(estudiante);
        documento.setVerificado(false);
        
        return documentoRepository.save(documento);
    }
    
    @Transactional
    public DocumentoIngresante verificarDocumento(Long idDocumento) throws Exception {
        DocumentoIngresante documento = documentoRepository.findById(idDocumento)
            .orElseThrow(() -> new Exception("Documento no encontrado"));
        
        documento.setVerificado(true);
        return documentoRepository.save(documento);
    }
    
    // Gestión de Anuncios
    public List<Anuncio> obtenerTodosAnuncios() {
        return anuncioRepository.findAll();
    }
    
    public List<Anuncio> obtenerAnunciosActivos() {
        return anuncioRepository.findByActivoTrueOrderByFechaCreacionDesc();
    }
    
    public List<Anuncio> obtenerAnunciosPorTipo(Anuncio.TipoAnuncio tipo) {
        return anuncioRepository.findByTipoAndActivoTrueOrderByFechaCreacionDesc(tipo);
    }
    
    @Transactional
    public Anuncio crearAnuncio(Anuncio anuncio) {
        anuncio.setActivo(true);
        anuncio.setFechaCreacion(java.time.LocalDateTime.now());
        return anuncioRepository.save(anuncio);
    }
    
    @Transactional
    public Anuncio actualizarAnuncio(Long id, Anuncio anuncioDetails) throws Exception {
        Anuncio anuncio = anuncioRepository.findById(id)
            .orElseThrow(() -> new Exception("Anuncio no encontrado"));
        
        anuncio.setTitulo(anuncioDetails.getTitulo());
        anuncio.setContenido(anuncioDetails.getContenido());
        anuncio.setTipo(anuncioDetails.getTipo());
        anuncio.setFechaInicio(anuncioDetails.getFechaInicio());
        anuncio.setFechaFin(anuncioDetails.getFechaFin());
        
        return anuncioRepository.save(anuncio);
    }
    
    @Transactional
    public void desactivarAnuncio(Long id) throws Exception {
        Anuncio anuncio = anuncioRepository.findById(id)
            .orElseThrow(() -> new Exception("Anuncio no encontrado"));
        
        anuncio.setActivo(false);
        anuncioRepository.save(anuncio);
    }
    
    // Gestión de Administradores
    public List<Administrador> obtenerTodosAdministradores() {
        return administradorRepository.findAll();
    }
    
    public Optional<Administrador> obtenerAdministradorPorUsuario(String usuario) {
        return administradorRepository.findByUsuario(usuario);
    }
    
    @Transactional
    public Administrador crearAdministrador(Administrador administrador) throws Exception {
        // Validar que el usuario no exista
        if (administradorRepository.findByUsuario(administrador.getUsuario()).isPresent()) {
            throw new Exception("El usuario ya existe");
        }
        
        administrador.setActivo(true);
        administrador.setFechaCreacion(java.time.LocalDateTime.now());
        
        return administradorRepository.save(administrador);
    }
    
    @Transactional
    public Administrador actualizarAdministrador(Long id, Administrador adminDetails) throws Exception {
        Administrador administrador = administradorRepository.findById(id)
            .orElseThrow(() -> new Exception("Administrador no encontrado"));
        
        // Si se cambia el usuario, validar que no exista
        if (!administrador.getUsuario().equals(adminDetails.getUsuario()) &&
            administradorRepository.findByUsuario(adminDetails.getUsuario()).isPresent()) {
            throw new Exception("El usuario ya existe");
        }
        
        administrador.setUsuario(adminDetails.getUsuario());
        administrador.setNombres(adminDetails.getNombres());
        administrador.setApellidos(adminDetails.getApellidos());
        administrador.setEmail(adminDetails.getEmail());
        administrador.setRol(adminDetails.getRol());
        
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            administrador.setPassword(adminDetails.getPassword());
        }
        
        return administradorRepository.save(administrador);
    }
    
    @Transactional
    public void cambiarEstadoAdministrador(Long id) throws Exception {
        Administrador administrador = administradorRepository.findById(id)
            .orElseThrow(() -> new Exception("Administrador no encontrado"));
        
        administrador.setActivo(!administrador.getActivo());
        administradorRepository.save(administrador);
    }
    
    // Estadísticas y reportes
    public BigDecimal calcularTotalRecaudado(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Pago> pagos = pagoRepository.findAll().stream()
            .filter(Pago::getValidado)
            .filter(p -> !p.getFechaPago().isBefore(fechaInicio) && !p.getFechaPago().isAfter(fechaFin))
            .toList();
        
        return pagos.stream()
            .map(Pago::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public long contarDocumentosPendientesVerificacion() {
        return documentoRepository.findAll().stream()
            .filter(d -> !d.getVerificado())
            .count();
    }
    
    public long contarPagosPendientesValidacion() {
        return pagoRepository.findByValidadoFalse().size();
    }
}
