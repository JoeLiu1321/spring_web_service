import application.*;
import application.adapter.output.DataOutputAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.SessionOutputAdapter;
import application.entities.Account;
import application.entities.AccountInfo;
import application.entities.Role;
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
    @Autowired
    private MessageOutputFactory messageOutputFactory;
    private Account account,inviteAccount;
    private AccountInfo accountInfo;
    private RequestBuilder addAccountRequest,deleteAccountRequest,loginRequest, getAllAccountRequest, getAccountRequest;
    private Session session;
    private Gson gson;
    @Before
    public void setUp(){
        gson=new Gson();
        String name="Joe",phone="0922",address="no.9"
                ,email="test@gmail.com",password="tim98765";
        inviteAccount=accountRepository.findByAccountName("admin").get();
        account=new Account("tim98765",password);
        accountInfo=new AccountInfo(name,phone,address,email);
        accountInfo.setInviteAccount(inviteAccount);
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
        getAllAccountRequest =get("/account")
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue());
        getAccountRequest=get("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue());
    }

    @After
    public void tearDown()throws Exception{
        Optional<Account> account=accountRepository.findByAccountName(this.account.getAccountName());
        if (account.isPresent())
            accountRepository.delete(account.get());
        redisService.deleteSession(this.account.getAccountName());
    }

    @Test
    public void deleteNotExistedAccount()throws Exception{
        mockMvc.perform(deleteAccountRequest).andExpect(content().string(gson.toJson(messageOutputFactory.dataNotFound("account"))));
    }

    @Test
    public void deleteExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(deleteAccountRequest)
                .andExpect(content().string(gson.toJson(messageOutputFactory.success())));
        assertEquals(Optional.empty(),accountRepository.findByAccountName(account.getAccountName()));
    }

    @Test
    public void addDuplicateAccount()throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().string(gson.toJson(messageOutputFactory.success())));
        this.mockMvc.perform(addAccountRequest).andExpect(content().string(gson.toJson(messageOutputFactory.accountHasBeenUsed())));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
    }

    @Test
    public void addAccount() throws Exception{
        this.mockMvc.perform(addAccountRequest).andExpect(content().json(gson.toJson(messageOutputFactory.success())));
        assertEquals(account.getAccountName(),accountRepository.findByAccountName(account.getAccountName()).get().getAccountName());
        assertEquals(account.getInfo().getAddress(),accountRepository.findByAccountName(account.getAccountName()).get().getInfo().getAddress());
        this.mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(new Account())))
                .andExpect(content().json(gson.toJson(messageOutputFactory.fieldNotPresent("accountName"))));
        this.mockMvc.perform(addAccountRequest).andExpect(content().json(gson.toJson(messageOutputFactory.accountHasBeenUsed())));
    }

    @Test
    public void getAllAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        List<Account> expectAccounts=accountRepository.findAll();
        MessageOutputAdapter messageOutputAdapter=new DataOutputAdapter(true,"success",expectAccounts.toArray(new Account[expectAccounts.size()]));
        mockMvc.perform(getAllAccountRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void getAccount()throws Exception{
        mockMvc.perform(getAccountRequest)
                .andExpect(content().json(gson.toJson(messageOutputFactory.dataNotFound("account"))));
        mockMvc.perform(addAccountRequest);
        MessageOutputAdapter messageOutputAdapter=new DataOutputAdapter(true,"success", new Account[]{account});
        mockMvc.perform(getAccountRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void login()throws Exception{

        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(messageOutputFactory.accountOrPasswordError())));
        mockMvc.perform(addAccountRequest);
        MessageOutputAdapter messageOutputAdapter =new SessionOutputAdapter(true,"success",session);
        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void logout()throws Exception{
        RequestBuilder logout=post("/account/logout")
                .param("accountName",account.getAccountName());
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(messageOutputFactory.accountOrPasswordError())));
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(messageOutputFactory.success())));
        assertFalse(redisService.isSessionExist(session));
    }

    @Test
    public void requestWithNoSession()throws Exception{
        redisService.deleteSession(account.getAccountName());
        mockMvc.perform(deleteAccountRequest)
            .andExpect(content().string(gson.toJson(messageOutputFactory.sessionTimeout())));
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account)))
                .andExpect(content().string(gson.toJson(messageOutputFactory.sessionTimeout())));
        mockMvc.perform(getAllAccountRequest)
                .andExpect(content().string(gson.toJson(messageOutputFactory.sessionTimeout())));
        mockMvc.perform(getAccountRequest)
                .andExpect(content().string(gson.toJson(messageOutputFactory.sessionTimeout())));
    }

    @Test
    public void updateAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        String newName="new Joe",newPhone="0922008112",newAddress="new Address"
                ,newEmail="newTest@gmail.com",newPassword="newPassword";
        Role newRole=new Role(RoleEnum.USER);
        AccountInfo newAccountInfo=new AccountInfo(newName,newPhone,newAddress,newEmail);
        newAccountInfo.setRole(newRole);
        this.account.setPassword(newPassword);
        this.account.setInfo(newAccountInfo);
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(this.account)))
                .andExpect(content().json(gson.toJson(messageOutputFactory.success())));

        String accountName=this.account.getAccountName();
        Account account=accountRepository.findByAccountName(accountName).get();
        AccountInfo actualAccountInfo=account.getInfo();
        assertEquals(newName,actualAccountInfo.getName());
        assertEquals(newPassword,account.getPassword());
        assertEquals(newRole,account.getInfo().getRole());
        assertEquals(newAddress,actualAccountInfo.getAddress());
        assertEquals(newEmail,actualAccountInfo.getEmail());
        assertEquals(newPhone,actualAccountInfo.getPhone());
    }

    @Test
    public void addAccountWithUpdate() throws Exception{
        RequestBuilder updateRequest=patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account));
        mockMvc.perform(updateRequest)
                .andExpect(content().string(gson.toJson(messageOutputFactory.success())));
        boolean isAccountExist=accountRepository.findByAccountName(account.getAccountName()).isPresent();
        assertTrue(isAccountExist);
    }
}
