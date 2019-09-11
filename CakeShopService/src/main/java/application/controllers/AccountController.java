package application.controllers;

import application.ResponseFactory;
import application.Session;
import application.RepositoryFacade;
import application.adapter.data.AccountAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.entities.Account;
import application.entities.AccountInfo;
import application.repositories.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/account")
public class AccountController {
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private AccountInfoRepository accountInfoRepository;
    @Autowired
    private ResponseFactory responseFactory;
    @RequestMapping(method = RequestMethod.GET, path="/test")
    public @ResponseBody String testRequest(){
        return "success";
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createAccount(@RequestBody Account account){
        Optional<String>accountName=Optional.ofNullable(account.getAccountName());
        if(!accountName.isPresent())
            return responseFactory.fieldNotPresent("accountName");
        else{
            boolean isUsed = facade.findAccount(accountName.get()).isPresent();
            if (isUsed)
                return responseFactory.accountHasBeenUsed();
            else {
                Integer accountId= facade.createAccount(account);
                return responseFactory.outputData(accountId);
            }
        }
    }

    @RequestMapping(path="/{accountName}", method = RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteAccount(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName){
        if(facade.isSessionExist(sessionKey,sessionValue)){
            Optional<Account> account= facade.findAccount(accountName);
            if(!account.isPresent())
                return responseFactory.dataNotFound("account");
            else {
                facade.deleteAccount(account.get().getAccountName());
                return responseFactory.success();
            }
        }
        else{
            return responseFactory.sessionTimeout();
        }
    }

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter login(@RequestParam String accountName, @RequestParam String password){
        if(facade.login(accountName,password)) {
            Session session= facade.createSession(accountName);
            return responseFactory.outputData(session);
        }
        else
            return responseFactory.accountOrPasswordError();
    }

    @RequestMapping(path="/logout/{accountName}", method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter logout(@PathVariable String accountName){
        Optional<Account> account= facade.findAccount(accountName);
        if(account.isPresent()) {
            facade.deleteSession(accountName);
            return responseFactory.success();
        }
        else
            return responseFactory.accountOrPasswordError();
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    MessageOutputAdapter getAll(@RequestParam String sessionKey, @RequestParam String sessionValue){
        if(!facade.isSessionExist(sessionKey,sessionValue)) {
            return responseFactory.sessionTimeout();
        }
        return responseFactory.outputData(outputAccounts(facade.findAccount()));
    }

    @RequestMapping(path="/{accountName}", method=RequestMethod.GET)
    public @ResponseBody MessageOutputAdapter getAccount(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName) {
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        Optional<Account> account = facade.findAccount(accountName);
        if (account.isPresent())
            return responseFactory.outputData(outputAccount(account.get()));
        else
            return responseFactory.dataNotFound("account");
    }

    @PatchMapping(path="/{accountName}")
    public @ResponseBody
    MessageOutputAdapter update(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName, @RequestBody Account account) {
        Optional<Account> existedAccount = facade.findAccount(accountName);
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else{
            if(!existedAccount.isPresent()){
                facade.createAccount(account);
                return responseFactory.outputData(account.getId());
            }
            else{
                Account updatedAccount = existedAccount.get();
                AccountInfo oldInfo=updatedAccount.getInfo();
                updatedAccount.setAccount(account);
                facade.createAccount(updatedAccount);
                accountInfoRepository.delete(oldInfo);
                return responseFactory.outputData(updatedAccount.getId());
            }
        }
    }

    public Iterable<AccountAdapter> outputAccounts(Iterable<Account>accounts){
        List<AccountAdapter> returnAccounts=new ArrayList<>();
        for(Account account:accounts)
            returnAccounts.add(outputAccount(account));
        return returnAccounts;
    }

    public AccountAdapter outputAccount(Account account){
        return new AccountAdapter(account);
    }

}
