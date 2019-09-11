import application.*;
import application.adapter.output.ObjectOutputAdapter;
import application.controllers.PaymentController;
import application.entities.Account;
import application.entities.Payment;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class PaymentTest {
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private ResponseFactory responseFactory;
    @Autowired
    private MockMvc mockMvc;
    private List<Integer> paymentIds;
    private Payment payment;
    private Session session;
    private Gson gson;
    @Before
    public void setUp(){
        gson=new Gson();
        paymentIds=new ArrayList<>();
        Account account = facade.findAccount("admin").get();
        session = facade.createSession(account.getAccountName());
        payment=new Payment("12:00",500, account);
    }

    @After
    public void tearDown(){
        for(Integer id:paymentIds)
            facade.deletePayment(id);
    }

    @Test
    public void addPaymentTest()throws Exception{
        String result=this.mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey", session.getKey())
                .param("sessionValue", session.getValue())
                .content(gson.toJson(payment)))
                .andReturn().getResponse().getContentAsString();
        Integer id=gson.fromJson(result, JsonObject.class).get("object").getAsInt();
        paymentIds.add(id);
        Optional<Payment> payment= facade.findPayment(id);
        assertTrue(payment.isPresent());
        assertEquals(this.payment.getTime(),payment.get().getTime());
        assertEquals(this.payment.getAccount().getAccountName(),payment.get().getAccount().getAccountName());
        assertEquals(this.payment.getPrice(),payment.get().getPrice());
    }

    @Test
    public void deletePaymentTest()throws Exception{
        Integer paymentId= facade.createPayment(payment);
        paymentIds.add(paymentId);
        this.mockMvc.perform(delete("/payment/"+paymentId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("sessionKey", session.getKey())
                .param("sessionValue", session.getValue()))
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        assertFalse(facade.findPayment(paymentId).isPresent());
    }

    @Test
    public void createPaymentTest(){
        ObjectOutputAdapter result=(ObjectOutputAdapter)paymentController.createPayment(session.getKey(),session.getValue(),payment);
        Integer id=(Integer)result.getObject();
        paymentIds.add(id);
        assertEquals(gson.toJson(responseFactory.outputData(id)),gson.toJson(result));
    }
}
