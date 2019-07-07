package application.repositories;

import application.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,Integer> {
    Optional<Product> findByName(String name);
}
