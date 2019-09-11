import application.*;
import application.adapter.data.AccountAdapter;
import application.adapter.output.MessageOutputAdapter;
import application.adapter.output.ObjectOutputAdapter;
import application.controllers.AccountController;
import application.entities.Account;
import application.entities.AccountInfo;
import application.entities.Role;
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
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AccountTest {
    @Autowired
    private AccountController accountController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RepositoryFacade facade;
    @Autowired
    private ResponseFactory responseFactory;
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
        inviteAccount= facade.findAccount("admin").get();
        account=new Account("tim98765",password);
        accountInfo=new AccountInfo(name,phone,address,email);
        accountInfo.setInviteAccount(inviteAccount);
        account.setInfo(accountInfo);
        addAccountRequest=post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account));
        session= facade.createSession(account.getAccountName());
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
        facade.deleteAccount(account.getAccountName());
        facade.deleteSession(session);
    }

    @Test
    public void deleteNotExistedAccount()throws Exception{
        mockMvc.perform(deleteAccountRequest).andExpect(content().string(gson.toJson(responseFactory.dataNotFound("account"))));
    }

    @Test
    public void deleteExistedAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(deleteAccountRequest)
                .andExpect(content().string(gson.toJson(responseFactory.success())));
        assertEquals(Optional.empty(), facade.findAccount(account.getAccountName()));
    }

    @Test
    public void createDuplicateAccount()throws Exception{
        facade.createAccount(account);
        this.mockMvc.perform(addAccountRequest)
                .andExpect(content().string(gson.toJson(responseFactory.accountHasBeenUsed())));
    }

    @Test
    public void createAccount() throws Exception{
        this.mockMvc.perform(addAccountRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        assertEquals(account.getAccountName(), facade.findAccount(account.getAccountName()).get().getAccountName());
        assertEquals(account.getInfo().getAddress(), facade.findAccount(account.getAccountName()).get().getInfo().getAddress());
        this.mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(new Account())))
                .andExpect(content().json(gson.toJson(responseFactory.fieldNotPresent("accountName"))));
        this.mockMvc.perform(addAccountRequest).andExpect(content().json(gson.toJson(responseFactory.accountHasBeenUsed())));
    }

    @Test
    public void getAllAccount()throws Exception{
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(getAllAccountRequest)
                .andExpect(content().json(gson.toJson(responseFactory.success())));
    }

    @Test
    public void getAccount()throws Exception{
        mockMvc.perform(getAccountRequest)
                .andExpect(content().json(gson.toJson(responseFactory.dataNotFound("account"))));
        Integer accountId= facade.createAccount(account);
        AccountAdapter expectedData=new AccountAdapter(facade.findAccount(accountId).get());
        MessageOutputAdapter expectedResult= responseFactory.outputData(expectedData);
        mockMvc.perform(getAccountRequest)
                .andExpect(content().json(gson.toJson(expectedResult)));
    }

    @Test
    public void login()throws Exception{
        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(responseFactory.accountOrPasswordError())));
        facade.createAccount(account);
        MessageOutputAdapter messageOutputAdapter =new ObjectOutputAdapter(true,"success",session);
        mockMvc.perform(loginRequest)
                .andExpect(content().json(gson.toJson(messageOutputAdapter)));
    }

    @Test
    public void logout()throws Exception{
        RequestBuilder logout=post("/account/logout/"+account.getAccountName());
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(responseFactory.accountOrPasswordError())));
        mockMvc.perform(addAccountRequest);
        mockMvc.perform(logout).andExpect(content().json(gson.toJson(responseFactory.success())));
        assertFalse(facade.isSessionExist(session.getKey(),session.getValue()));
    }

    @Test
    public void requestWithNoSession()throws Exception{
        facade.deleteSession(session);
        mockMvc.perform(deleteAccountRequest)
            .andExpect(content().string(gson.toJson(responseFactory.sessionTimeout())));
        mockMvc.perform(patch("/account/"+account.getAccountName())
                .param("sessionKey",session.getKey())
                .param("sessionValue",session.getValue())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(account)))
                .andExpect(content().string(gson.toJson(responseFactory.sessionTimeout())));
        mockMvc.perform(getAllAccountRequest)
                .andExpect(content().string(gson.toJson(responseFactory.sessionTimeout())));
        mockMvc.perform(getAccountRequest)
                .andExpect(content().string(gson.toJson(responseFactory.sessionTimeout())));
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
                .andExpect(content().json(gson.toJson(responseFactory.success())));

        String accountName=this.account.getAccountName();
        Account account= facade.findAccount(accountName).get();
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
                .andExpect(content().json(gson.toJson(responseFactory.success())));
        boolean isAccountExist= facade.findAccount(account.getAccountName()).isPresent();
        assertTrue(isAccountExist);
    }

    @Test
    public void creteAccountTest(){
        MessageOutputAdapter expectedResult= responseFactory.success();
        MessageOutputAdapter actualResult=accountController.createAccount(account);
        assertEquals(expectedResult.getStatus(),actualResult.getStatus());
        assertEquals(expectedResult.getMessage(),actualResult.getMessage());
    }

    @Test
    public void getAccountTest(){
        accountController.createAccount(account);
        MessageOutputAdapter expectedResult= responseFactory.success();
        ObjectOutputAdapter actualResult=(ObjectOutputAdapter) accountController.getAccount(session.getKey(),session.getValue(),account.getAccountName());
        AccountAdapter actualAccount=(AccountAdapter) actualResult.getObject();
        assertEquals(expectedResult.getStatus(),actualResult.getStatus());
        assertEquals(account.getAccountName(),actualAccount.getAccountName());
    }
}
