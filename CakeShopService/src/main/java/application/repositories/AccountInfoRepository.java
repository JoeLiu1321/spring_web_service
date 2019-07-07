package application.repositories;
import application.entities.AccountInfo;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AccountInfoRepository extends CrudRepository<AccountInfo, Integer> {

}