package application.controllers;

import application.ResponseMessage;
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

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseMessage createOrder(@RequestBody Order order){
        order.setFinish(false);
        order.setPay(false);
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        order.setTime(dateTimeFormatter.format(localDateTime));
        orderRepository.save(order);
        return new ResponseMessage(true,"success");
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
    public @ResponseBody ResponseMessage deleteOrder(@PathVariable Integer id){
        try {
            Optional<Order> order=orderRepository.findById(id);
            orderRepository.delete(order.get());
            return new ResponseMessage(true,"success");
        }catch (Exception e){
            return new ResponseMessage(false,"order not found");
        }
    }

}

