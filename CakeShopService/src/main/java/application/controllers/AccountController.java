package application.controllers;

import application.MessageOutputFactory;
import application.RedisService;
import application.Session;
import application.adapter.output.DataOutputAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.SessionOutputAdapter;
import application.entities.Account;
import application.entities.AccountInfo;
import application.repositories.AccountInfoRepository;
import application.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountInfoRepository accountInfoRepository;
    @Autowired
    private RedisService sessionService;
    @Autowired
    private MessageOutputFactory messageOutputFactory;
    @RequestMapping(method = RequestMethod.GET, path="/test")
    public @ResponseBody String testRequest(){
        return "success";
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createAccount(@RequestBody Optional<Account> account){
        Optional<String>accountName=Optional.ofNullable(account.get().getAccountName());
        if(!accountName.isPresent())
            return messageOutputFactory.fieldNotPresent("accountName");
        else{
            Account newAccount=account.get();
            Boolean isUsed = accountRepository.findByAccountName(newAccount.getAccountName()).isPresent();
            if (isUsed)
                return messageOutputFactory.accountHasBeenUsed();
            else {
                accountRepository.save(newAccount);
                return messageOutputFactory.success();
            }
        }
    }

    @RequestMapping(path="/{accountName}", method = RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteAccount(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName){
        if(sessionService.isSessionExist(sessionKey,sessionValue)){
            Optional<Account> account=accountRepository.findByAccountName(accountName);
            if(!account.isPresent())
                return messageOutputFactory.dataNotFound("account");
            else {
                accountRepository.delete(account.get());
                return messageOutputFactory.success();
            }
        }
        else{
            return messageOutputFactory.sessionTimeout();
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter login(@RequestParam String accountName, @RequestParam String password){
        Optional<Account> account=accountRepository.findByAccountNameAndPassword(accountName,password);
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false);
        if(account.isPresent()) {
            Session session=sessionService.createSession(accountName);
            return new SessionOutputAdapter(true,"success",session);
        }
        else
            return messageOutputFactory.accountOrPasswordError();
    }

    @RequestMapping(path="/logout", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter logout(@RequestParam String accountName){
        Optional<Account> account=accountRepository.findByAccountName(accountName);
        if(account.isPresent()) {
            sessionService.deleteSession(accountName);
            return messageOutputFactory.success();
        }
        else
            return messageOutputFactory.accountOrPasswordError();
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    MessageOutputAdapter getAll(@RequestParam String sessionKey, @RequestParam String sessionValue){
        if(!sessionService.isSessionExist(sessionKey,sessionValue)) {
            return messageOutputFactory.sessionTimeout();
        }
        List<Account>accounts=accountRepository.findAll();
        return new DataOutputAdapter(true,"success",accounts.toArray(new Account[accounts.size()]));
    }

    @RequestMapping(path="/{accountName}", method=RequestMethod.GET)
    public @ResponseBody MessageOutputAdapter getAccount(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName) {
        if(!sessionService.isSessionExist(sessionKey,sessionValue))
            return messageOutputFactory.sessionTimeout();
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (account.isPresent())
            return new DataOutputAdapter(true,"success", new Account[]{account.get()});
        else
            return messageOutputFactory.dataNotFound("account");
    }

    @PatchMapping(path="/{accountName}")
    public @ResponseBody
    MessageOutputAdapter update(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName, @RequestBody Account account) {
        Optional<Account> existedAccount = accountRepository.findByAccountName(accountName);
        if(!sessionService.isSessionExist(sessionKey,sessionValue))
            return messageOutputFactory.sessionTimeout();
        else{
            if(!existedAccount.isPresent())
                accountRepository.save(account);
            else{
                Account updatedAccount = existedAccount.get();
                AccountInfo oldInfo=updatedAccount.getInfo();
                updatedAccount.setAccount(account);
                accountRepository.save(updatedAccount);
                accountInfoRepository.delete(oldInfo);
            }
            return messageOutputFactory.success();
        }
    }
    public MessageOutputAdapter checkAccount(Account account){
        String errorMsg="";
        return new MessageOutputAdapter(false,errorMsg);
    }

}
