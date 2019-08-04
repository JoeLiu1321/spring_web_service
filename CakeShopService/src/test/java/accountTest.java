import application.Application;
import application.RedisService;
import application.Session;
import application.adapter.output.AccountOutputAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.SessionOutputAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class accountTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AccountRepository accountRepository;
    private Account account;
    private AccountInfo accountInfo;
    private RequestBuilder addAccountRequest,deleteAccountRequest,loginRequest,getAccountRequest;
    private Session session;
    private Gson gson;
    @Before
    public void setUp(){
        gson=new Gson();
        String name="Joe",phone="0922",address="no.9"
                ,email="test@gmail.com",password="tim98765";
        account=new Account("tim98765",password);
        accountInfo=new AccountInfo(name,phone,address,email);
        account.setInfo(accountInfo);
        addAccountRequest=post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account));
        session=redisService.createSession(account.getAccountName());
        loginRequest=post("/account/login")
                .param("accountName",account.getAccountName())
                .param("password",account.getPassword());
        deleteAccountRequest=delete("/account/"+account.getAccountName())
            .param("sessionKey",session.getKey())
            .param("sessionValue",session.getValue());
        getAccountRequest=get("/account")
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue());
    }

    @After
    public void tearDown()throws Exception{
        mockMvc.perform(deleteAccountRequest);
        redisService.deleteSession(account.getAccountName());
    }

    @Test
    public void deleteNotExistedAccount()throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false,"Account not found");
        mockMvc.perform(deleteAccountRequest).andExpect(content().string(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void deleteExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(true,"success");
        mockMvc.perform(deleteAccountRequest)
                .andExpect(content().string(gson.toJson(messageOutputAdapter)));
        assertEquals(Optional.empty(),accountRepository.findByAccountName(account.getAccountName()));
    }

    @Test
    public void addDuplicateAccount()throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(true,"success");
        this.mockMvc.perform(addAccountRequest).andExpect(content().string(gson.toJson(messageOutputAdapter)));
        messageOutputAdapter =new MessageOutputAdapter(false,"This account had been used");
        this.mockMvc.perform(addAccountRequest).andExpect(content().string(gson.toJson(messageOutputAdapter)));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
    }

    @Test
    public void addAccount() throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(true,"success");
        this.mockMvc.perform(addAccountRequest).andExpect(content().json(gson.toJson(messageOutputAdapter)));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
        assertEquals(account.getInfo().getAddress(),accountRepository.findByAccountName(account.getAccountName()).get().getInfo().getAddress());
        messageOutputAdapter =new MessageOutputAdapter(false,"Account Name is not present");
        this.mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(new Account())))
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
        messageOutputAdapter.setMessage("This account had been used");
        this.mockMvc.perform(addAccountRequest).andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void getAccount()throws Exception{
        List<Account> accounts=new ArrayList<>();
        accounts.add(account);
        MessageOutputAdapter messageOutputAdapter=new AccountOutputAdapter(true,"success",accounts);
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(getAccountRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void login()throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false,"Account or Password Error");
        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
        mockMvc.perform(addAccountRequest);
        messageOutputAdapter =new SessionOutputAdapter(true,"success",session);
        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void logout()throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false,"Account or Password Error");
        RequestBuilder logout=post("/account/logout")
                .param("accountName",account.getAccountName());
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(messageOutputAdapter)));
        mockMvc.perform(addAccountRequest);
        messageOutputAdapter =new MessageOutputAdapter(true,"success");
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(messageOutputAdapter)));
        assertFalse(redisService.isSessionExist(session));
    }

    @Test
    public void requestWithNoSession()throws Exception{
        redisService.deleteSession(account.getAccountName());
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(false,"Session Timeout");
        mockMvc.perform(deleteAccountRequest)
            .andExpect(content().string(gson.toJson(messageOutputAdapter)));
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account)))
                .andExpect(content().string(gson.toJson(messageOutputAdapter)));
        mockMvc.perform(getAccountRequest)
                .andExpect(content().string(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void updateAccount()throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(true,"success");
        mockMvc.perform(addAccountRequest);
        String newName="new Joe",newPhone="0922008112",newAddress="new Address"
                ,newEmail="newTest@gmail.com",newPassword="newPassword";
        Integer newPrivilege=0;
        this.account.setPrivilege(newPrivilege);
        this.account.setPassword(newPassword);
        this.account.setInfo(new AccountInfo(newName,newPhone,newAddress,newEmail));
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(this.account)))
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));

        String accountName=this.account.getAccountName();
        Account account=accountRepository.findByAccountName(accountName).get();
        AccountInfo accountInfo=account.getInfo();
        assertEquals(newName,accountInfo.getName());
        assertEquals(newPassword,account.getPassword());
        assertEquals(newPrivilege,account.getPrivilege());
        assertEquals(newAddress,accountInfo.getAddress());
        assertEquals(newEmail,accountInfo.getEmail());
        assertEquals(newPhone,accountInfo.getPhone());
    }

    @Test
    public void addAccountWithUpdate() throws Exception{
        MessageOutputAdapter messageOutputAdapter =new MessageOutputAdapter(true,"success");
        RequestBuilder updateRequest=patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account));
        mockMvc.perform(updateRequest)
                .andExpect(content().string(gson.toJson(messageOutputAdapter)));
        boolean isAccountExist=accountRepository.findByAccountName(account.getAccountName()).isPresent();
        assertTrue(isAccountExist);
    }
}
