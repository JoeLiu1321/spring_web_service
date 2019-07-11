package application.controllers;

import application.entities.Account;
import application.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String createAccount(@RequestBody Optional<Account> account){
        Optional<String>accountName=Optional.ofNullable(account.get().getAccountName());
        if(!accountName.isPresent())
            return "Account Name is not present";
        else{
            Account newAccount=account.get();
            Boolean isUsed = accountRepository.findByAccountName(newAccount.getAccountName()).isPresent();
            if (isUsed)
                return "This account had been used";
            else {
//                newAccount.setPrivilege(1);
                accountRepository.save(newAccount);
                return "success";
            }
        }
    }

    @RequestMapping(path="/{accountName}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteAccount(@PathVariable String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(!account.isPresent())
            return "Fail:Account not found";
        else {
            accountRepository.delete(account.get());
            return "success";
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestParam String accountName, @RequestParam String password){
        Optional<Account> account=accountRepository.findByAccountNameAndPassword(accountName,password);
        if(account.isPresent())
            return "success";
        else
            return "fail";
    }

    @RequestMapping(path="/logout", method = RequestMethod.POST)
    public @ResponseBody String logout(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(account.isPresent())
            return "success";
        else
            return "fail";
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Iterable<Account> getAll(){
        return accountRepository.findAll();
    }

    @RequestMapping(path="/{accountName}", method=RequestMethod.GET)
    public @ResponseBody Account getAccount(@PathVariable String accountName) {
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (account.isPresent())
            return account.get();
        else
            return null;
    }

    @PatchMapping(path="/{accountName}")
    public @ResponseBody String update(@PathVariable String accountName, @RequestBody Account account) {
        Optional<Account> existedAccount = accountRepository.findByAccountName(accountName);
        if (existedAccount.isPresent()) {
            Account updatedAccount=existedAccount.get();
            updatedAccount.setAccount(account);
            accountRepository.save(updatedAccount);
            return "success";
        }
        else
            return "fail";
    }

}
