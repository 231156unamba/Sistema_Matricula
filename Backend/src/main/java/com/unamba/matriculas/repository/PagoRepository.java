package com.unamba.matriculas.repository;

import com.unamba.matriculas.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByVoucherIgnoreCase(String voucher);
    Optional<Pago> findByVoucher(String voucher);
    Optional<Pago> findByVoucherAndValidadoTrue(String voucher);
    List<Pago> findByEstudiante_IdEstudianteAndTipoPagoAndValidadoTrue(Long idEstudiante, Pago.TipoPago tipoPago);
    List<Pago> findByEstudiante_IdEstudiante(Long idEstudiante);
    Optional<Pago> findFirstByEstudiante_IdEstudianteOrderByFechaPagoDesc(Long idEstudiante);
    List<Pago> findByValidadoFalse();
    List<Pago> findByTipoPago(Pago.TipoPago tipoPago);
}
