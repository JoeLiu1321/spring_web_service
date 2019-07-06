import application.Application;
import application.entities.Product;
import application.repositories.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class productTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    private RequestBuilder createProductRequest,deleteProductRequest;
    private String productName="Test Product",productDescription="This is the test product";
    private Integer productPrice=1000;
    @Before
    public void setUp(){
        createProductRequest=post("/product/create")
                .param("name",productName)
                .param("description",productDescription)
                .param("price",productPrice.toString());
        deleteProductRequest=delete("/product/delete")
                .param("id","1");
    }
    @After
    public void tearDown()throws Exception{
        this.mockMvc.perform(deleteProductRequest);
    }
    @Test
    public void addProduct()throws Exception{
        this.mockMvc.perform(createProductRequest).andExpect(content().string("success"));
        Optional<Product>product=productRepository.findById(1);
        assertEquals(productName, product.get().getName());
        assertEquals(productDescription, product.get().getDescription());
        assertEquals(productPrice, product.get().getPrice());
        assertFalse(product.get().getOnSell());
    }
}
