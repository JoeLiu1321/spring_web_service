import application.Application;
import application.entities.Account;
import application.entities.AccountInfo;
import application.repositories.AccountRepository;
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
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class accountTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    private Account account;
    private RequestBuilder addAccountRequest,deleteAccountRequest;
    @Before
    public void setUp(){
        String name="Joe",phone="0922",address="no.9"
                ,email="test@gmail.com",password="tim98765";
        account=new Account("tim98765",password);
        AccountInfo accountInfo=new AccountInfo(name,phone,address,email);
        account.setInfo(accountInfo);
        addAccountRequest=post("/account/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(account));
        deleteAccountRequest=delete("/account/delete")
                .param("accountName",account.getAccountName());
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
        assertEquals(Optional.empty(),accountRepository.findByAccountName(account.getAccountName()));
    }

    @Test
    public void addDuplicateAccount()throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("success"));
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("This account had been used"));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
        assertEquals("inValid",accountRepository.findByAccountName(account.getAccountName()).get().getPrivilege());
    }

    @Test
    public void addAccount() throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("success"));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
        assertEquals("inValid",accountRepository.findByAccountName(account.getAccountName()).get().getPrivilege());
        this.mockMvc.perform(post("/account/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(new Account())))
                .andExpect(content().string("Account Name is not present"));
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("This account had been used"));
    }

    @Test
    public void validateExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(get("/account/validate").param("accountName",account.getAccountName())).andExpect(content().string("success validate"));
        assertEquals("user", accountRepository.findByAccountName(account.getAccountName()).get().getPrivilege());
    }

    @Test
    public void getAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(get("/account/get")
                .param("accountName",account.getAccountName()))
                .andExpect(content().json(new Gson().toJson(account)));
    }


}
