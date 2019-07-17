package application.controllers;

import application.entities.Order;
import application.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    String createOrder(@RequestBody Order order){
        orderRepository.save(order);
        return "success";
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
    public @ResponseBody String deleteOrder(@PathVariable Integer id){
        try {
            Optional<Order> order=orderRepository.findById(id);
            orderRepository.delete(order.get());
            return "success";
        }catch (Exception e){
//            return e.getMessage();
            return "fail:order not found";
        }
    }

}

