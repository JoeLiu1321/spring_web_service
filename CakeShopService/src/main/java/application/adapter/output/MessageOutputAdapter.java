package application.adapter.output;

public class MessageOutputAdapter {
    private Boolean status;
    private String message;

    public MessageOutputAdapter(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public MessageOutputAdapter(){

    }

    public MessageOutputAdapter(Boolean status){
        this.status=status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
