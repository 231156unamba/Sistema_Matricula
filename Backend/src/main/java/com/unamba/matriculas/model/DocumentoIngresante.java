package com.unamba.matriculas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "documentos_ingresante")
public class DocumentoIngresante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;
    
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;
    
    @Column(name = "declaracion_jurada")
    private String declaracionJurada;
    
    @Column(name = "certificado_estudios")
    private String certificadoEstudios;
    
    @Column(name = "boleta_matricula")
    private String boletaMatricula;
    
    @Column(name = "boleta_examen")
    private String boletaExamen;
    
    @Column(name = "pago_centro_medico")
    private String pagoCentroMedico;
    
    @Column(name = "hoja_matricula")
    private String hojaMatricula;
    
    @Column(name = "verificado")
    private Boolean verificado = false;

    public DocumentoIngresante() {}

    public DocumentoIngresante(Long idDocumento, Estudiante estudiante, String declaracionJurada, String certificadoEstudios, String boletaMatricula, String boletaExamen, String pagoCentroMedico, String hojaMatricula, Boolean verificado) {
        this.idDocumento = idDocumento;
        this.estudiante = estudiante;
        this.declaracionJurada = declaracionJurada;
        this.certificadoEstudios = certificadoEstudios;
        this.boletaMatricula = boletaMatricula;
        this.boletaExamen = boletaExamen;
        this.pagoCentroMedico = pagoCentroMedico;
        this.hojaMatricula = hojaMatricula;
        this.verificado = verificado;
    }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public String getDeclaracionJurada() { return declaracionJurada; }
    public void setDeclaracionJurada(String declaracionJurada) { this.declaracionJurada = declaracionJurada; }
    public String getCertificadoEstudios() { return certificadoEstudios; }
    public void setCertificadoEstudios(String certificadoEstudios) { this.certificadoEstudios = certificadoEstudios; }
    public String getBoletaMatricula() { return boletaMatricula; }
    public void setBoletaMatricula(String boletaMatricula) { this.boletaMatricula = boletaMatricula; }
    public String getBoletaExamen() { return boletaExamen; }
    public void setBoletaExamen(String boletaExamen) { this.boletaExamen = boletaExamen; }
    public String getPagoCentroMedico() { return pagoCentroMedico; }
    public void setPagoCentroMedico(String pagoCentroMedico) { this.pagoCentroMedico = pagoCentroMedico; }
    public String getHojaMatricula() { return hojaMatricula; }
    public void setHojaMatricula(String hojaMatricula) { this.hojaMatricula = hojaMatricula; }
    public Boolean getVerificado() { return verificado; }
    public void setVerificado(Boolean verificado) { this.verificado = verificado; }
}
