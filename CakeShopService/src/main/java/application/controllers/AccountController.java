package application.controllers;

import application.RedisService;
import application.Session;
import application.adapter.output.AccountOutputAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.SessionOutputAdapter;
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
    @Autowired
    private RedisService sessionService;
    @RequestMapping(method = RequestMethod.GET, path="/test")
    public @ResponseBody String testRequest(){
        return "success";
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createAccount(@RequestBody Optional<Account> account){
        Optional<String>accountName=Optional.ofNullable(account.get().getAccountName());
        if(!accountName.isPresent())
            return new MessageOutputAdapter(false,"Account Name is not present");
        else{
            Account newAccount=account.get();
            Boolean isUsed = accountRepository.findByAccountName(newAccount.getAccountName()).isPresent();
            if (isUsed)
                return new MessageOutputAdapter(false,"This account had been used");
            else {
                accountRepository.save(newAccount);
                return new MessageOutputAdapter(true,"success");
            }
        }
    }

    @RequestMapping(path="/{accountName}", method = RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteAccount(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName){
        if(sessionService.isSessionExist(sessionKey,sessionValue)){
            Optional<Account> account=accountRepository.findByAccountName(accountName);
            if(!account.isPresent())
                return new MessageOutputAdapter(false,"Account not found");
            else {
                accountRepository.delete(account.get());
                return new MessageOutputAdapter(true,"success");
            }
        }
        else{
            return new MessageOutputAdapter(false,"Session Timeout");
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter login(@RequestParam String accountName, @RequestParam String password){
        Optional<Account> account=accountRepository.findByAccountNameAndPassword(accountName,password);
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false);
        if(account.isPresent()) {
            Session session=sessionService.createSession(accountName);
            return new SessionOutputAdapter(true, "success",session);
        }
        else
            return new MessageOutputAdapter(false,"Account or Password Error");
    }

    @RequestMapping(path="/logout", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter logout(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(account.isPresent()) {
            sessionService.deleteSession(accountName);
            return new MessageOutputAdapter(true, "success");
        }
        else
            return new MessageOutputAdapter(false,"Account or Password Error");
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    MessageOutputAdapter getAll(@RequestParam String sessionKey, @RequestParam String sessionValue){
        if(!sessionService.isSessionExist(sessionKey,sessionValue)) {
            return new MessageOutputAdapter(false,"Session Timeout");
        }
        return new AccountOutputAdapter(true,"success",accountRepository.findAll());
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
    public @ResponseBody
    MessageOutputAdapter update(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName, @RequestBody Account account) {
        Optional<Account> existedAccount = accountRepository.findByAccountName(accountName);
        if(!sessionService.isSessionExist(sessionKey,sessionValue))
            return new MessageOutputAdapter(false,"Session Timeout");
        else{
            if(!existedAccount.isPresent())
                accountRepository.save(account);
            else{
                Account updatedAccount = existedAccount.get();
                updatedAccount.setAccount(account);
                accountRepository.save(updatedAccount);
            }
            return new MessageOutputAdapter(true, "success");
        }
    }

}
