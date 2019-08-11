package application.controllers;

import application.MessageOutputFactory;
import application.RedisService;
import application.adapter.output.MessageOutputAdapter;
import application.Session;
import application.entities.Order;
import application.repositories.OrderRepository;
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
    private OrderRepository orderRepository;
    @Autowired
    private RedisService sessionService;
    @Autowired
    private MessageOutputFactory messageOutputFactory;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @RequestBody Order order){
        Session s=new Session(sessionKey,sessionValue);
        if(!sessionService.isSessionExist(s))
            return messageOutputFactory.sessionTimeout();
        order.setFinish(false);
        order.setPay(false);
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        order.setTime(dateTimeFormatter.format(localDateTime));
        orderRepository.save(order);
        return messageOutputFactory.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Iterable<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<Order> getOrder(@PathVariable Integer id){
        return orderRepository.findById(id);
    }

    @RequestMapping(path="/{id}", method=RequestMethod.DELETE)
    public @ResponseBody
    MessageOutputAdapter deleteOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable Integer id){
        if(!sessionService.isSessionExist(sessionKey,sessionValue))
            return messageOutputFactory.sessionTimeout();
        else {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                orderRepository.delete(order.get());
                return messageOutputFactory.success();
            } else
                return messageOutputFactory.dataNotFound("order");
        }
    }

}

