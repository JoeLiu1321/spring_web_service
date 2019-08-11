package application;

import application.entities.Account;
import application.entities.AccountInfo;
import application.entities.Role;
import application.repositories.AccountRepository;
import application.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public CommandLineRunner setUp(RoleRepository roleRepository, AccountRepository accountRepository) {
        return (args -> {
            for(RoleEnum roleEnum:RoleEnum.values()) {
                if(!roleRepository.findById(roleEnum.ordinal()).isPresent())
                    roleRepository.save(new Role(roleEnum.ordinal(),roleEnum.name()));
            }

            String accountName="admin",password="password",
                    name="Boss",phone="0987654321",address="Taipei"
                    ,email="admin@gmail.com";
            if(!accountRepository.findByAccountName(accountName).isPresent()) {
                AccountInfo accountInfo = new AccountInfo(name, phone, address, email);
                accountInfo.setRole(new Role(RoleEnum.ADMIN));
                Account account = new Account(accountName, password);
                account.setInfo(accountInfo);
                accountRepository.save(account);
            }
        });
    }
}
