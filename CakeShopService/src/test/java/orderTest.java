import application.Application;
import application.RedisService;
import application.adapter.output.MessageOutputAdapter;
import application.Session;
import application.entities.Order;
import application.entities.Product;
import application.repositories.OrderRepository;
import application.repositories.ProductRepository;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class orderTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedisService redisService;
    private RequestBuilder createOrderRequest,deleteOrderRequest;
    private Order order;
    private Product product;
    private Gson gson;
    private Session session;
    @Before
    public void setUp(){
        String sessionKey="t123";
        session=redisService.createSession(sessionKey);
        gson=new Gson();
        product = new Product("Cake","TEST",1000);
        order=new Order(product,"GP","08:56");
        order.setPay(true);
        order.setFinish(true);
        productRepository.save(product);
        createOrderRequest=post("/order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .content(gson.toJson(order));

    }
    @After
    public void tearDown()throws Exception {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        redisService.deleteSession(session.getKey());
    }

    @Test
    public void addOrder()throws Exception{
        this.mockMvc.perform(createOrderRequest).andExpect(content().json(gson.toJson(new MessageOutputAdapter(true,"success"))));
        Order order=orderRepository.findAll().iterator().next();
        assertEquals("GP", order.getAccountName());
        assertEquals(false,order.getFinish());
        assertEquals(false,order.getPay());
        this.redisService.deleteSession(session.getKey());
        this.mockMvc.perform(createOrderRequest)
                .andExpect(content().json(gson.toJson(new MessageOutputAdapter(false,"Session Timeout"))));
    }

    @Test
    public void deleteOrder()throws Exception{
        this.mockMvc.perform(createOrderRequest);
        Integer orderId=orderRepository.findAll().iterator().next().getId();
        deleteOrderRequest=delete("/order/"+orderId)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        this.mockMvc.perform(deleteOrderRequest)
                .andExpect(content().json(gson.toJson(new MessageOutputAdapter(true,"success"))));
        assertFalse(orderRepository.findById(orderId).isPresent());
        this.mockMvc.perform(deleteOrderRequest)
                .andExpect(content().json(gson.toJson(new MessageOutputAdapter(false,"order not found"))));
    }
}
