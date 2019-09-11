import application.Application;
import application.ResponseFactory;
import application.Session;
import application.RepositoryFacade;
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
public class ProductTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private RepositoryFacade facade;
    private String productName="Test Product",productDescription="This is the test product";
    private Product product;
    private Integer productPrice=1000;
    private Gson gson;
    private RequestBuilder createProductRequest;
    private List<Integer> productIds;
    private Session session;
    @Before
    public void setUp(){
        session= facade.createSession("testSession");
        productIds=new ArrayList<>();
        product=new Product(productName,productDescription,productPrice);
        createProductRequest=post("/product")
                .param("sessionKey", session.getKey())
                .param("sessionValue", session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(product));
        gson=new Gson();
    }
    @After
    public void tearDown()throws Exception{
        for(Integer id:productIds)
            facade.deleteProduct(id);
        facade.deleteSession(session);
    }

    @Test
    public void addDuplicateProduct()throws Exception{
        productIds.add(facade.createProduct(product));
        String result=this.mockMvc.perform(createProductRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())))
                .andReturn().getResponse().getContentAsString();
        Integer id=gson.fromJson(result, JsonObject.class).get("object").getAsInt();
        productIds.add(id);
        assertTrue(facade.findProduct(id).isPresent());
    }

    @Test
    public void addProduct()throws Exception{
        String result=this.mockMvc.perform(createProductRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())))
                .andReturn().getResponse().getContentAsString();
        Integer id=gson.fromJson(result,JsonObject.class).get("object").getAsInt();
        productIds.add(id);
        Optional<Product>product= facade.findProduct(id);
        assertTrue(product.isPresent());
        assertEquals(productName, product.get().getName());
        assertEquals(productDescription, product.get().getDescription());
        assertEquals(productPrice, product.get().getPrice());
        assertFalse(product.get().getOnSell());
    }

    @Test
    public void deleteProduct()throws Exception{
        Integer id= facade.createProduct(product);
        productIds.add(id);
        this.mockMvc.perform(delete("/product/"+id)
                .param("sessionKey", session.getKey())
                .param("sessionValue", session.getValue()))
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        assertFalse(facade.findProduct(id).isPresent());
    }

    @Test
    public void updateProduct()throws Exception{
        String newDescription="New Description";
        Integer id= facade.createProduct(product);
        productIds.add(id);
        product.setDescription(newDescription);
        this.mockMvc.perform(patch("/product/"+id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey", session.getKey())
                .param("sessionValue", session.getValue())
                .content(gson.toJson((product))))
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        assertTrue(facade.findProduct(id).isPresent());
        assertEquals(newDescription, facade.findProduct(id).get().getDescription());
    }

    @Test
    public void findProduct()throws Exception{
        Integer id= facade.createProduct(product);
        productIds.add(id);
        Optional<Product> expectedProduct= facade.findProduct(id);
        this.mockMvc.perform(get("/product/"+id))
                .andExpect(content().json(gson.toJson(responseFactory.outputData(expectedProduct.get()))));

    }


}
