import application.Application;
import application.RedisService;
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
public class RedisServiceTest {
    @Autowired
    private RedisService redisService;
    private String accountName ="testKey";
    private Session session;
    @Before
    public void setUp(){
        session=redisService.createSession(accountName);
    }
    @After
    public void tearDown(){
        redisService.deleteSession(session.getKey());
    }

    @Test
    public void createSessionTest(){
        assertTrue(redisService.isSessionExist(session));

    }

    @Test
    public void deleteSessionTest(){
        redisService.deleteSession(accountName);
        assertFalse(redisService.isSessionExist(session));
    }

}
