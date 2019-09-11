package application.repositories;

import application.entities.Payment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment,Integer> {
    List<Payment> findAll();
}
