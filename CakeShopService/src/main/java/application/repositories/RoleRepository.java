package application.repositories;

import application.entities.AccountInfo;
import application.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
