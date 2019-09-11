package application.repositories;

import application.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order,Integer> {
    Optional<Order> findByIdAndIsPay(Integer integer,boolean isPay);
    Iterable<Order> findAllByIdAndIsPay(Iterable<Integer> integer, boolean isPay);
    Iterable<Order> findAllByAccountNameAndIsPay(String accountName,boolean isPay);
    Iterable<Order> findAllByAccountName(String accountName);
}
