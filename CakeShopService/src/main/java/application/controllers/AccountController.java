package application.controllers;

import application.entities.Account;
import application.entities.Privilege;
import application.entities.AccountInfo;
import application.repositories.AccountRepository;
import application.repositories.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountInfoRepository accountInfoRepository;
//    @RequestMapping(path = "/create", method = RequestMethod.POST)
//    public @ResponseBody String createAccount(@RequestParam String accountName, @RequestParam String password, @RequestParam String email, @RequestParam String name, @RequestParam String phone, @RequestParam String address){
//        Optional<Account> account=accountRepository.findByAccountName(accountName);
//        if(account.isPresent())
//            return "This account had been used";
//        else{
//            Account newAccount=new Account(accountName,password);
//            accountRepository.save(newAccount);
//            AccountInfo accountInfo =new AccountInfo(accountName,name,phone,address,email);
//            accountInfoRepository.save(accountInfo);
//            return "success";
//        }
//    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
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
                newAccount.setPrivilege(new Privilege(0, "inValid"));
                accountRepository.save(newAccount);
                return "success";
            }
        }
    }


    @RequestMapping(path="/delete", method = RequestMethod.DELETE)
    public @ResponseBody String deleteAccount(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(!account.isPresent())
            return "Fail:Account not found";
        else {
            accountRepository.delete(account.get());
//            AccountInfo accountInfo = accountInfoRepository.findByAccount(accountName);
//            accountInfoRepository.delete(accountInfo);
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

    @RequestMapping(path="/validate", method = RequestMethod.GET)
    public @ResponseBody String validateAccount(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(account.isPresent()){
            Account validateAccount=account.get();
            validateAccount.setPrivilege(new Privilege(1,"user"));
            accountRepository.save(validateAccount);
            return "success validate";
        }
        else
            return "fail to validate";
    }

    @RequestMapping(path="/get", method=RequestMethod.GET)
    public @ResponseBody Iterable<Account> getAll(){
        return accountRepository.findAll();
    }

    @RequestMapping(path="/get", method=RequestMethod.GET, params = {"accountName"})
    public @ResponseBody Account getAccount(@Param("accountName") String accountName) {
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (account.isPresent())
            return account.get();
        else
            return null;
    }

    @RequestMapping(method= RequestMethod.PUT, params = {"account","newPwd"})
    public @ResponseBody String setPassword(@RequestParam("account") String accountId, @RequestParam("newPwd") String newPwd){
        Optional<Account> account=accountRepository.findByAccountName(accountId);
        if(account.isPresent()) {
            Account updateAccount = account.get();
            updateAccount.setPassword(newPwd);
            accountRepository.save(updateAccount);
            return "success";
        }
        else
            return "fail:account not found";
    }
}
