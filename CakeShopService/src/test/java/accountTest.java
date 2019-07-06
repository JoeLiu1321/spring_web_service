import application.Application;
import application.repositories.AccountRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class accountTest {
    private String testAccount="tim98765";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    private RequestBuilder addAccountRequest,deleteAccountRequest;
    @Before
    public void setUp(){
        String name="Joe",phone="0922",address="no.9"
                ,email="test@gmail.com",password="tim98765";
        addAccountRequest=post("/account/create")
                .param("accountName",testAccount)
                .param("name",name)
                .param("phone",phone)
                .param("address",address)
                .param("email",email)
                .param("password",password);
        deleteAccountRequest=delete("/account/delete")
                .param("accountName","tim98765");
    }

    @After
    public void tearDown()throws Exception{
        mockMvc.perform(deleteAccountRequest);
    }

    @Test
    public void deleteNotExistedAccount()throws Exception{
        mockMvc.perform(deleteAccountRequest).andExpect(content().string("Fail:Account not found"));
    }

    @Test
    public void deleteExistedAccount()throws Exception{
        mockMvc.perform(deleteAccountRequest).andExpect(content().string("Fail:Account not found"));
        assertNull(accountRepository.findByAccount(testAccount));
    }

    @Test
    public void addDuplicateAccount()throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("success"));
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("This account had been used"));
        assertEquals(testAccount,accountRepository.findByAccount(testAccount).getAccount());
        assertEquals("inValid",accountRepository.findByAccount(testAccount).getPrivilege());
    }

    @Test
    public void addAccount() throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("success"));
        assertEquals(testAccount,accountRepository.findByAccount(testAccount).getAccount());
        assertEquals("inValid",accountRepository.findByAccount(testAccount).getPrivilege());
    }

    @Test
    public void validateExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(get("/account/validate").param("accountName",testAccount)).andExpect(content().string("success validate"));
        assertEquals("user", accountRepository.findByAccount(testAccount).getPrivilege());
    }


}
