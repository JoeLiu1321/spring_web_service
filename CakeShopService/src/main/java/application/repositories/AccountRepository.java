package application.repositories;

import application.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Optional<Account> findByAccountName(String accountName);
    Optional<Account> findByAccountNameAndPassword(String accountName, String password);
}
