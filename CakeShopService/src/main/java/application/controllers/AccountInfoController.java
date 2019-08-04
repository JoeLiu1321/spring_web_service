package application.controllers;

import application.entities.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import application.repositories.AccountInfoRepository;

@Controller
@RequestMapping(path="/user")
public class AccountInfoController {
    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<AccountInfo> getAllUsers() {
        return accountInfoRepository.findAll();
    }


}