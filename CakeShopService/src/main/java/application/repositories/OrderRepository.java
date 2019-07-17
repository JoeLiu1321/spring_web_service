package application.repositories;

import application.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Integer> {

}
