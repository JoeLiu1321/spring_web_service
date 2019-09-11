import application.Application;
import application.ResponseFactory;
import application.Session;
import application.RepositoryFacade;
import application.entities.Order;
import application.entities.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class OrderTest {
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResponseFactory responseFactory;
    private Order order;
    private Gson gson;
    private Session session;
    private List<Integer> orderIds;
    private List<Integer> productIds;
    @Before
    public void setUp(){
        orderIds=new ArrayList<>();
        productIds=new ArrayList<>();
        session= facade.createSession("testSession");
        gson=new Gson();
        Product product = new Product("Cake", "TEST", 1000);
        order=new Order(product,3,"GP","08:56");
        productIds.add(facade.createProduct(product));
    }
    @After
    public void tearDown()throws Exception {
        for(Integer id:orderIds)
            facade.deleteOrder(id);
        for(Integer id:productIds)
            facade.deleteProduct(id);
        facade.deleteSession(session);
    }

    @Test
    public void addOrderTest()throws Exception{
        RequestBuilder createOrderRequest=post("/order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .content(gson.toJson(order));
        String result=this.mockMvc.perform(createOrderRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())))
                .andReturn().getResponse().getContentAsString();
        JsonObject jsonObject=gson.fromJson(result,JsonObject.class);
        Integer id=jsonObject.get("object").getAsInt();
        orderIds.add(id);
        Optional<Order> order= facade.findOrder(id);
        assertTrue(order.isPresent());
    }

    @Test
    public void deleteOrderTest()throws Exception{
        Integer orderId= facade.createOrder(order);
        orderIds.add(orderId);
        RequestBuilder deleteOrderRequest=delete("/order/"+orderId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue());
        this.mockMvc.perform(deleteOrderRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        assertFalse(facade.findOrder(orderId).isPresent());
    }

    @Test
    public void getOrderTest()throws Exception{
        Integer orderId= facade.createOrder(order);
        orderIds.add(orderId);
        RequestBuilder getOrderRequest=get("/order/"+orderId)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        this.mockMvc.perform(getOrderRequest)
            .andExpect(content().json(gson.toJson(responseFactory.success())));
    }

}
