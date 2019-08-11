package application.adapter.output;

import application.adapter.data.AccountAdapter;
import application.adapter.data.DataAdapter;
import application.entities.Account;
import java.util.ArrayList;
import java.util.List;

public class DataOutputAdapter extends MessageOutputAdapter{
    private List<DataAdapter> datas;

    public DataOutputAdapter(boolean status, String message){
        super(status,message);
        init();
    }

    public DataOutputAdapter(boolean status, String message, Account[]accounts){
        this(status,message);
        setData(accounts);
    }

    public void init(){
        datas =new ArrayList<>();
    }

    public void setData(Account[] accounts){
        init();
        for(Account account:accounts)
            datas.add(new AccountAdapter(account));
    }

    public List<DataAdapter> getDatas() {
        return datas;
    }


}
