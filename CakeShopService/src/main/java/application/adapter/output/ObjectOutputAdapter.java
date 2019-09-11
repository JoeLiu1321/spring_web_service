package application.adapter.output;

import application.Session;

public class ObjectOutputAdapter extends MessageOutputAdapter{
    private Object object;

    public ObjectOutputAdapter(){

    }

    public ObjectOutputAdapter(Boolean status, String message, Object object) {
        super(status, message);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
