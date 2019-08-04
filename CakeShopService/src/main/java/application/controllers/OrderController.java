package application.controllers;

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
    private RedisService redisService;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    MessageOutputAdapter createOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @RequestBody Order order){
        Session s=new Session(sessionKey,sessionValue);
        if(!redisService.isSessionExist(s))
            return new MessageOutputAdapter(false,"Session Timeout");
        order.setFinish(false);
        order.setPay(false);
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        order.setTime(dateTimeFormatter.format(localDateTime));
        orderRepository.save(order);
        return new MessageOutputAdapter(true,"success");
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
    MessageOutputAdapter deleteOrder(@PathVariable Integer id){
        Optional<Order> order=orderRepository.findById(id);
        if(order.isPresent()) {
            orderRepository.delete(order.get());
            return new MessageOutputAdapter(true, "success");
        }
        else
            return new MessageOutputAdapter(false,"order not found");
    }

}

