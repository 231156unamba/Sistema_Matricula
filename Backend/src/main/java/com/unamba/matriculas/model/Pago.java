package com.unamba.matriculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
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

    public Pago() {}

    public Pago(Long idPago, Estudiante estudiante, TipoPago tipoPago, String voucher, BigDecimal monto, LocalDate fechaPago, Boolean validado) {
        this.idPago = idPago;
        this.estudiante = estudiante;
        this.tipoPago = tipoPago;
        this.voucher = voucher;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.validado = validado;
    }

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public TipoPago getTipoPago() { return tipoPago; }
    public void setTipoPago(TipoPago tipoPago) { this.tipoPago = tipoPago; }
    public String getVoucher() { return voucher; }
    public void setVoucher(String voucher) { this.voucher = voucher; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public Boolean getValidado() { return validado; }
    public void setValidado(Boolean validado) { this.validado = validado; }
}
