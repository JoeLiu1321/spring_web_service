import application.Application;
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

import static org.junit.Assert.assertEquals;
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
    private RequestBuilder createOrderRequest,deleteOrderRequest;
    private Order order;
    private Product product;
    @Before
    public void setUp(){
        product = new Product("Cake","TEST",1000);
//        productRepository.save(product);
        order=new Order(product,"GP","08:56","BUY");
        createOrderRequest=post("/order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(order));
//        orderRepository.findAll()
//        deleteOrderRequest=delete("/order/"+productName);
    }
//    @After
//    public void tearDown()throws Exception{
//        this.mockMvc.perform(deleteProductRequest);

    @Test
    public void addProduct()throws Exception{
        this.mockMvc.perform(createOrderRequest).andExpect(content().string("success"));
        Order order=orderRepository.findAll().iterator().next();
        assertEquals("GP", order.getAccountName());
//        assertEquals(productDescription, product.get().getDescription());
//        assertEquals(productPrice, product.get().getPrice());
//        assertFalse(product.get().getOnSell());
    }
}
