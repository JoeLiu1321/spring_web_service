package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    private long timeout;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public RedisService(){
        timeout=15;
    }
    public RedisService(long timeout){
        this.timeout=timeout;
    }

    public Session createSession(String key){
        Session session=new Session(key,getUUID());
        ValueOperations operations = redisTemplate.opsForValue();
        if(redisTemplate.hasKey(key)) {
            String oldSession=operations.get(key).toString();
            session.setValue(oldSession);
            operations.set(key,oldSession);
        }
        else
            operations.set(key, session.getValue());
        redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
        return session;
    }

    public void keepSession(Session session){
        String key=session.getKey();
        ValueOperations operations=redisTemplate.opsForValue();
        redisTemplate.expire(key,timeout,TimeUnit.MINUTES);
    }

    public boolean isSessionExist(Session session){
        String key=session.getKey(),value=session.getValue();
        if(!isSessionKeyExist(key))
            return false;
        else{
            return value.equals(redisTemplate.opsForValue().get(key));
        }
    }

    public boolean isSessionExist(String key, String value){
        if(!isSessionKeyExist(key))
            return false;
        else{
            return value.equals(redisTemplate.opsForValue().get(key));
        }
    }

    public String getUUID(){
        return UUID.randomUUID().toString().substring(0,12);
    }

    public boolean isSessionKeyExist(String key){
        return redisTemplate.hasKey(key);
    }

    public void deleteSession(String key){
        if (isSessionKeyExist(key))
            redisTemplate.delete(key);
    }
}
