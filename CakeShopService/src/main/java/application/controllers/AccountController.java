package application.controllers;

import application.ResponseMessage;
import application.entities.Account;
import application.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping(method = RequestMethod.GET, path="/test")
    public @ResponseBody String testRequest(){
        return "success";
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseMessage createAccount(@RequestBody Optional<Account> account){
        Optional<String>accountName=Optional.ofNullable(account.get().getAccountName());
        if(!accountName.isPresent())
            return new ResponseMessage(false,"Account Name is not present");
        else{
            Account newAccount=account.get();
            Boolean isUsed = accountRepository.findByAccountName(newAccount.getAccountName()).isPresent();
            if (isUsed)
                return new ResponseMessage(false,"This account had been used");
            else {
                accountRepository.save(newAccount);
                return new ResponseMessage(true,"success");
            }
        }
    }

    @RequestMapping(path="/{accountName}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseMessage deleteAccount(@PathVariable String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(!account.isPresent())
            return new ResponseMessage(false,"Account not found");
        else {
            accountRepository.delete(account.get());
            return new ResponseMessage(true,"success");
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage login(@RequestParam String accountName, @RequestParam String password){
        Optional<Account> account=accountRepository.findByAccountNameAndPassword(accountName,password);
        ResponseMessage responseMessage=new ResponseMessage(false);
        if(account.isPresent())
            return new ResponseMessage(true,"success");
        else
            return new ResponseMessage(false,"Account or Password Error");
    }

    @RequestMapping(path="/logout", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage logout(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(account.isPresent())
            return new ResponseMessage(true,"success");
        else
            return new ResponseMessage(false,"Account or Password Error");
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
    public @ResponseBody ResponseMessage update(@PathVariable String accountName, @RequestBody Account account) {
        Optional<Account> existedAccount = accountRepository.findByAccountName(accountName);
        if (existedAccount.isPresent()) {
            Account updatedAccount=existedAccount.get();
            updatedAccount.setAccount(account);
            accountRepository.save(updatedAccount);
            return new ResponseMessage(true,"success");
        }
        else
            return new ResponseMessage(false,"fail");
    }

}
