package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;
    
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;
    
    @NotNull(message = "El tipo de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago")
    private TipoPago tipoPago;
    
    @NotBlank(message = "El voucher es obligatorio")
    @Size(max = 50)
    @Column(name = "voucher", length = 50)
    private String voucher;
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    @Column(name = "monto", precision = 8, scale = 2)
    private BigDecimal monto;
    
    @NotNull(message = "La fecha de pago es obligatoria")
    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
    
    @Column(name = "validado")
    private Boolean validado = false;
    
    public enum TipoPago {
        MATRICULA, EXAMEN, CENTRO_MEDICO
    }
}
