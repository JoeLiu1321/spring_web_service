package application.repositories;

import application.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findByAccount(String account);
    Account findByAccountAndPassword(String account, String password);
}
