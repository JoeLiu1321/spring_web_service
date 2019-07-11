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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
    private AccountInfo accountInfo;
    private RequestBuilder addAccountRequest,deleteAccountRequest,loginRequest;
    private MockHttpServletRequestBuilder updateRequest;
    @Before
    public void setUp(){
        String name="Joe",phone="0922",address="no.9"
                ,email="test@gmail.com",password="tim98765";
        account=new Account("tim98765",password);
        accountInfo=new AccountInfo(name,phone,address,email);
        account.setInfo(accountInfo);
        addAccountRequest=post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(account));
        loginRequest=post("/account/login")
                .param("accountName",account.getAccountName())
                .param("password",account.getPassword());
        deleteAccountRequest=delete("/account/"+account.getAccountName());
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
    }

    @Test
    public void addAccount() throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("success"));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
        this.mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(new Account())))
                .andExpect(content().string("Account Name is not present"));
        this.mockMvc.perform(addAccountRequest).andExpect(content().string("This account had been used"));
    }

    @Test
    public void getAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(get("/account/"+account.getAccountName()))
                .andExpect(content().json(new Gson().toJson(account)));
    }

    @Test
    public void login()throws Exception{
        mockMvc.perform(loginRequest)
                .andExpect(content().string("fail"));
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(loginRequest)
                .andExpect(content().string("success"));
    }

    @Test
    public void logout()throws Exception{
        RequestBuilder logout=post("/account/logout")
                .param("accountName",account.getAccountName());
        mockMvc.perform(logout).andExpect(content().string("fail"));
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(logout).andExpect(content().string("success"));
    }

    @Test
    public void inValidateExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        account.setPrivilege(0);
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(account)))
                .andExpect(content().string("success"));
        assertEquals(0,accountRepository.findByAccountName(account.getAccountName()).get().getPrivilege(),0);
    }

    @Test
    public void setPassword()throws Exception{
        mockMvc.perform(addAccountRequest);
        String newPassword="newPasswordTim98765";
        account.setPassword(newPassword);
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(account)))
                .andExpect(content().string("success"));
        assertEquals(newPassword,accountRepository.findByAccountName(account.getAccountName()).get().getPassword());
    }

    @Test
    public void updateAccountInfo()throws Exception{
        mockMvc.perform(addAccountRequest);
        accountInfo.setName("newNAme");
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(account)))
                .andExpect(content().string("success"));
        assertEquals("newNAme",accountRepository.findByAccountName(account.getAccountName()).get().getInfo().getName());
    }
}
