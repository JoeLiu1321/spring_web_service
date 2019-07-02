package application.controllers;

import application.entities.Account;
import application.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String addAccount(@RequestParam String account, @RequestParam String password){
        Account newAccount=new Account(account,password);
        accountRepository.save(newAccount);
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET, params = "account")
    public @ResponseBody Account getAccount(@RequestParam("account") String account){
        return accountRepository.findByAccount(account);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Account> getAccount(){
        return accountRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody String deleteAccount(@RequestParam String account){
        Account accountToDelete=accountRepository.findByAccount(account);
        accountRepository.delete(accountToDelete);
        return "success";
    }

    @RequestMapping(method= RequestMethod.PUT, params = {"account","newPwd"})
    public @ResponseBody String setPassword(@RequestParam("account") String account, @RequestParam("newPwd") String newPwd){
        Account updateAccount=accountRepository.findByAccount(account);
        updateAccount.setPassword(newPwd);
        accountRepository.save(updateAccount);
        return "success";
    }
}
