package application.controllers;

import application.ResponseFactory;
import application.RepositoryFacade;
import application.RoleEnum;
import application.adapter.output.MessageOutputAdapter;
import application.entities.Order;
import application.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private RepositoryFacade facade;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @RequestBody Order order){
        if(!facade.isSessionExist(sessionKey, sessionValue))
            return responseFactory.sessionTimeout();
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        order.setTime(dateTimeFormatter.format(localDateTime));
        Integer orderId=facade.createOrder(order);
        return responseFactory.outputData(orderId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody MessageOutputAdapter getAllOrder(@RequestParam String sessionKey, @RequestParam String sessionValue){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else
            return responseFactory.outputData(facade.findOrder());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MessageOutputAdapter getOrder(@PathVariable Integer id){
        Optional<Order>order=facade.findOrder(id);
        if(order.isPresent())
            return responseFactory.outputData(order.get());
        else
            return responseFactory.dataNotFound("order");
    }

    @RequestMapping(path = "/{accountName}", method = RequestMethod.GET, params = {"sessionKey","sessionValue"})
    public @ResponseBody
    MessageOutputAdapter getOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable String accountName){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else {
            Role userRole=facade.findAccount(sessionKey).get().getInfo().getRole();
            if(userRole.isEqual(RoleEnum.ADMIN) || userRole.isEqual(RoleEnum.EMPLOYEE))
                return responseFactory.outputData(facade.findOrder(accountName));
            else
                return responseFactory.outputData(facade.findOrder(sessionKey));
        }
    }

    @RequestMapping(path="/{id}", method=RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable Integer id){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else {
            Optional<Order> order = facade.findOrder(id);
            if (order.isPresent()) {
                facade.deleteOrder(id);
                return responseFactory.success();
            } else
                return responseFactory.dataNotFound("order");
        }
    }

}

