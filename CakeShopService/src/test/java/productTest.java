import application.Application;
import application.entities.Product;
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
    private Product product;
    private Integer productPrice=1000;
    @Before
    public void setUp(){
        product=new Product(productName,productDescription,productPrice);
        createProductRequest=post("/product")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(product));
        deleteProductRequest=delete("/product/"+productName);
    }
    @After
    public void tearDown()throws Exception{
        this.mockMvc.perform(deleteProductRequest);
    }

    @Test
    public void addDuplicateProduct()throws Exception{
        this.mockMvc.perform(createProductRequest);
        Optional<Product> product=productRepository.findByName(productName);
        assertEquals(productName,product.get().getName());
        this.mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(product.get()))).andExpect(content().string("success"));
    }

    @Test
    public void addProduct()throws Exception{
        this.mockMvc.perform(createProductRequest).andExpect(content().string("success"));
        Optional<Product>product=productRepository.findByName(productName);
        assertEquals(productName, product.get().getName());
        assertEquals(productDescription, product.get().getDescription());
        assertEquals(productPrice, product.get().getPrice());
        assertFalse(product.get().getOnSell());
    }

    @Test
    public void deleteProduct()throws Exception{
        this.mockMvc.perform(createProductRequest);
        this.mockMvc.perform(deleteProductRequest).andExpect(content().string("success"));
        this.mockMvc.perform(deleteProductRequest).andExpect(content().string("fail:product not found"));
    }

    @Test
    public void updateProduct()throws Exception{
        Product product=new Product();
        product.setName("OldName");
        productRepository.save(product);
        product=productRepository.findByName("OldName").get();
        product.setDescription("New Description");
        this.mockMvc.perform(patch("/product/"+product.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson((product))))
                .andExpect(content().string("success"));
        assertEquals("New Description",productRepository.findAll().iterator().next().getDescription());
    }

    @Test
    public void findProduct()throws Exception{
        this.mockMvc.perform(createProductRequest);
        Product product=productRepository.findByName(productName).get();
        Integer productId=product.getId();
        this.mockMvc.perform(get("/product/"+productId))
                .andExpect(content().json(new Gson().toJson(product)));
    }


}
