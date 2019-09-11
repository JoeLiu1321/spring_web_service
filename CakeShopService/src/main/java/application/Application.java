package application;

import application.entities.Account;
import application.entities.AccountInfo;
import application.entities.Product;
import application.entities.Role;
import application.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public CommandLineRunner setUp(RoleRepository roleRepository, RepositoryFacade facade) {
        return (args -> {
            createDefaultRoles(roleRepository);
            createDefaultAccount(facade);
            createDefaultProduct(facade);

        });
    }

    private void createDefaultRoles(RoleRepository roleRepository){
        for(RoleEnum roleEnum:RoleEnum.values()) {
            if(!roleRepository.findById(roleEnum.ordinal()).isPresent())
                roleRepository.save(new Role(roleEnum.ordinal(),roleEnum.name()));
        }
    }

    private void createDefaultAccount(RepositoryFacade facade){
        String accountName="admin",password="password",
                name="Boss",phone="0987654321",address="Taipei"
                ,email="admin@gmail.com";
        if(!facade.findAccount(accountName).isPresent()) {
            AccountInfo accountInfo = new AccountInfo(name, phone, address, email);
            accountInfo.setRole(new Role(RoleEnum.ADMIN));
            Account account = new Account(accountName, password);
            account.setInfo(accountInfo);
            facade.createAccount(account);
        }
    }

    private void createDefaultProduct(RepositoryFacade facade){
        String[]productNames=new String[]{"草莓蛋糕","輕乳酪蛋糕","測試蛋糕"};
        Integer productPrice=200;
        if(!facade.findProduct().iterator().hasNext()){
            for(String productName:productNames){
                Product product=new Product(productName,"這是"+productName,productPrice);
                product.setOnSell(true);
                product.setVolume(500);
                facade.createProduct(product);
            }
        }
    }
}
