package application;
import application.entities.Account;
import application.entities.Order;
import application.entities.Payment;
import application.entities.Product;
import application.repositories.AccountRepository;
import application.repositories.OrderRepository;
import application.repositories.PaymentRepository;
import application.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositoryFacade {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private SessionService sessionService;

    public RepositoryFacade() {
    }

    public Integer createOrder(Order order){
        orderRepository.save(order);
        return order.getId();
    }

    public Iterable<Order>findOrder(String accountName){
        return orderRepository.findAllByAccountName(accountName);
    }

    public Optional<Order> findOrder(Integer id){
        return orderRepository.findById(id);
    }

    public Iterable<Order> findOrder(){
        return orderRepository.findAll();
    }
    public void deleteOrder(Integer orderId){
        if(orderRepository.findById(orderId).isPresent())
            orderRepository.deleteById(orderId);
    }

    public Session createSession(String key){
        return sessionService.createSession(key);
    }

    public boolean isSessionExist(String sessionKey, String sessionValue){
        return sessionService.isSessionExist(sessionKey,sessionValue);
    }

    public void deleteSession(Session session){
        deleteSession(session.getKey());
    }

    public void deleteSession(String sessionKey){
        sessionService.deleteSession(sessionKey);
    }

    public Integer createProduct(Product product){
        productRepository.save(product);
        return product.getId();
    }

    public Optional<Product> findProduct(Integer id){
        return productRepository.findById(id);
    }

    public Iterable<Product> findProduct(){
        return productRepository.findAll();
    }

    public void deleteProduct(Integer id){
        if(findProduct(id).isPresent())
            productRepository.deleteById(id);
    }

    public Integer createAccount(Account account){
        accountRepository.save(account);
        return account.getId();
    }

    public Optional<Account> findAccount(String accountName){
        return accountRepository.findByAccountName(accountName);
    }

    public Iterable<Account> findAccount(){
        return accountRepository.findAll();
    }

    public Optional<Account> findAccount(Integer id){
        return accountRepository.findById(id);
    }

    public boolean login(String accountName, String password){
        return accountRepository.findByAccountNameAndPassword(accountName,password).isPresent();
    }

    public void deleteAccount(String accountName){
        Optional<Account>account=findAccount(accountName);
        account.ifPresent(value -> accountRepository.delete(value));
    }

    public Integer createPayment(Payment payment){
        paymentRepository.save(payment);
        return payment.getId();
    }

    public Optional<Payment> findPayment(Integer id){
        return paymentRepository.findById(id);
    }

    public void deletePayment(Integer id){
        if(findPayment(id).isPresent())
            paymentRepository.deleteById(id);
    }


}
