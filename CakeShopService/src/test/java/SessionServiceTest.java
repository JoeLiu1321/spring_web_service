import application.Application;
import application.SessionService;
import application.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SessionServiceTest {
    @Autowired
    private SessionService sessionService;
    private String accountName ="testKey";
    private Session session;
    @Before
    public void setUp(){
        session= sessionService.createSession(accountName);
    }
    @After
    public void tearDown(){
        sessionService.deleteSession(session.getKey());
    }

    @Test
    public void createSessionTest(){
        assertTrue(sessionService.isSessionExist(session));
    }

    @Test
    public void deleteSessionTest(){
        sessionService.deleteSession(accountName);
        assertFalse(sessionService.isSessionExist(session));
    }

}
