package application.controllers;

import application.entities.Account;
import application.entities.Privilege;
import application.entities.User;
import application.repositories.AccountRepository;
import application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public @ResponseBody String createAccount(@RequestParam String accountName, @RequestParam String password, @RequestParam String email, @RequestParam String name, @RequestParam String phone, @RequestParam String address){
        Account account=accountRepository.findByAccount(accountName);
        if(account!=null)
            return "This account had been used";
        else{
            account=new Account(accountName,password);
            accountRepository.save(account);
            User user=new User(accountName,name,phone,address,email);
            userRepository.save(user);
            return "success";
        }
    }

    @RequestMapping(path="/delete", method = RequestMethod.DELETE)
    public @ResponseBody String deleteAccount(@RequestParam String accountName){
        Account account=accountRepository.findByAccount(accountName);
        if(account==null)
            return "Fail:Account not found";
        else {
            accountRepository.delete(account);
            User user = userRepository.findByAccount(accountName);
            userRepository.delete(user);
            return "success";
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestParam String accountName, @RequestParam String password){
        Account account=accountRepository.findByAccountAndPassword(accountName,password);
        if(account!=null)
            return "success";
        else
            return "fail";
    }

    @RequestMapping(path="/logout", method = RequestMethod.POST)
    public @ResponseBody String logout(@RequestParam String accountName){
        Account account=accountRepository.findByAccount(accountName);
        if(account!=null)
            return "success";
        else
            return "fail";
    }

    @RequestMapping(path="/validate", method = RequestMethod.GET)
    public @ResponseBody String validateAccount(@RequestParam String accountName){
        Account account=accountRepository.findByAccount(accountName);
        if(account!=null){
            account.setPrivilege(new Privilege(1,"user"));
            accountRepository.save(account);
            return "success validate";
        }
        else
            return "fail to validate";
    }

    @RequestMapping(path="/all", method=RequestMethod.GET)
    public @ResponseBody Iterable<Account> getAll(){
        return accountRepository.findAll();
    }

    @RequestMapping(method= RequestMethod.PUT, params = {"account","newPwd"})
    public @ResponseBody String setPassword(@RequestParam("account") String account, @RequestParam("newPwd") String newPwd){
        Account updateAccount=accountRepository.findByAccount(account);
        updateAccount.setPassword(newPwd);
        accountRepository.save(updateAccount);
        return "success";
    }
}
