package application;

public class Session {
    private String key;
    private String value;

    public Session(){
        setKey("");
        setValue("");
    }

    public Session(String key) {
        setKey(key);
        setValue("");
    }

    public Session(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
