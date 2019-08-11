package application;

import application.adapter.output.MessageOutputAdapter;
import org.springframework.stereotype.Component;

@Component
public class MessageOutputFactory {
    public MessageOutputFactory() {

    }

    public MessageOutputAdapter sessionTimeout(){
        return new MessageOutputAdapter(false,"Session Timeout");
    }

    public MessageOutputAdapter accountHasBeenUsed(){
        return new MessageOutputAdapter(false,"This account had been used");
    }

    public MessageOutputAdapter dataNotFound(String data){
        return new MessageOutputAdapter(false,data+" not found");
    }

    public MessageOutputAdapter success(){
        return new MessageOutputAdapter(true,"success");
    }

    public MessageOutputAdapter accountOrPasswordError(){
        return new MessageOutputAdapter(false,"Account or password error");
    }

    public MessageOutputAdapter fieldNotPresent(String field){
        return new MessageOutputAdapter(false,field+" is not present");
    }

}
