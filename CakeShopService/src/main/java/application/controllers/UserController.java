package application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import application.entities.User;
import application.repositories.UserRepository;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String addNewUser (@RequestParam String account, @RequestParam String name, @RequestParam String phone, @RequestParam String address, @RequestParam String email) {
        User n = new User(account,name,phone,address,email);
        userRepository.save(n);
        return "Saved";
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody String deleteUser(@RequestParam String account){
        return "success";
    }
}