package app.repository;

import app.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    List<Payment> findAll();
    Payment findPaymentByEmployeeAndPeriod(String email, String period);
    List<Payment> findAllByEmployeeOrderByPeriodDesc(String email);
    boolean existsByEmployeeAndPeriod(String email, String period);
}
