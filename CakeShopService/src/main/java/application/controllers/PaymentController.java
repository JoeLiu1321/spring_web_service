package application.controllers;

import application.ResponseFactory;
import application.RepositoryFacade;
import application.adapter.output.MessageOutputAdapter;
import application.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@CrossOrigin
@RequestMapping(path="/payment")
public class PaymentController {
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private ResponseFactory responseFactory;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody MessageOutputAdapter createPayment(@RequestParam String sessionKey, @RequestParam String sessionValue,@RequestBody Payment payment){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else{
            facade.createPayment(payment);
            return responseFactory.outputData(payment.getId());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path="/{paymentId}")
    public @ResponseBody MessageOutputAdapter deletePayment(@RequestParam String sessionKey, @RequestParam String sessionValue, @PathVariable Integer paymentId){
        if(!facade.isSessionExist(sessionKey,sessionValue))
            return responseFactory.sessionTimeout();
        else{
            facade.deletePayment(paymentId);
            return responseFactory.success();
        }
    }

//    @RequestMapping(method = RequestMethod.POST, path="/pay")
//    public @ResponseBody MessageOutputAdapter payForOrder(@RequestParam String sessionKey, @RequestParam String sessionValue, @RequestBody List<Integer>orderIds) {
//        if (!helper.isSessionExist(sessionKey, sessionValue))
//            return messageOutputFactory.sessionTimeout();
//        else {
//            Iterable<Order> orders = helper.findAllById(orderIds);
//            Payment payment = new Payment();
//            int price = 0;
//            for (Order order : orders) {
//                if (!Optional.ofNullable(order.getPayment()).isPresent()) {
//                    price += (order.getProduct().getPrice()) * order.getNumber();
//                    order.setPayment(payment);
//                }
//            }
//            Account account = accountRepository.findByAccountName(sessionKey).get();
//            payment.setAccount(account);
//            payment.setPrice(price);
//            payment.setTime(getCurrentTime());
//            paymentRepository.save(payment);
//            for (Order order : orders)
//                if (!Optional.ofNullable(order.getPayment()).isPresent())
//                    order.setPayment(payment);
//            orderRepository.saveAll(orders);
//            return messageOutputFactory.success();
//            }
//        }

    private String getCurrentTime(){
        LocalDateTime localDateTime=LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        return dateTimeFormatter.format(localDateTime);
    }


}
