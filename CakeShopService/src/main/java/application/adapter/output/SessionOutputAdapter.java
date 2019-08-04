package application.adapter.output;

import application.Session;

public class SessionOutputAdapter extends MessageOutputAdapter{
    private Session session;

    public SessionOutputAdapter(){

    }

    public SessionOutputAdapter(Boolean status, String message, Session session) {
        super(status, message);
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
