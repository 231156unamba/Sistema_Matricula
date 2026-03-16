package com.unamba.matriculas.dto;

import jakarta.validation.constraints.*;

public class MatriculaIngresanteRequest {
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String dni;
    
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;
    
    @NotBlank(message = "El voucher es obligatorio")
    private String voucher;
    
    @NotBlank(message = "La declaración jurada es obligatoria")
    private String declaracionJurada;
    
    @NotBlank(message = "El certificado de estudios es obligatorio")
    private String certificadoEstudios;
    
    @NotBlank(message = "La boleta de matrícula es obligatoria")
    private String boletaMatricula;
    
    @NotBlank(message = "La boleta de examen es obligatoria")
    private String boletaExamen;
    
    @NotBlank(message = "El pago de centro médico es obligatorio")
    private String pagoCentroMedico;
    
    @NotBlank(message = "La hoja de matrícula es obligatoria")
    private String hojaMatricula;

    public MatriculaIngresanteRequest() {}

    public MatriculaIngresanteRequest(String dni, String nombres, String apellidos, String voucher, String declaracionJurada, String certificadoEstudios, String boletaMatricula, String boletaExamen, String pagoCentroMedico, String hojaMatricula) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.voucher = voucher;
        this.declaracionJurada = declaracionJurada;
        this.certificadoEstudios = certificadoEstudios;
        this.boletaMatricula = boletaMatricula;
        this.boletaExamen = boletaExamen;
        this.pagoCentroMedico = pagoCentroMedico;
        this.hojaMatricula = hojaMatricula;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getVoucher() { return voucher; }
    public void setVoucher(String voucher) { this.voucher = voucher; }
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
}
