package com.unamba.matriculas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "documentos_ingresante")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
