package application.adapter.output;

import application.adapter.AccountAdapter;
import application.entities.Account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountOutputAdapter extends MessageOutputAdapter{
    private List<AccountAdapter> accounts;
    public AccountOutputAdapter(){
        init();
    }

    public AccountOutputAdapter(Boolean status, String message) {
        super(status, message);
        init();
    }

    public AccountOutputAdapter(Boolean status, String message, Iterable<Account> accounts) {
        super(status, message);
        parse(accounts);
    }

    public void init(){
       accounts=new ArrayList<>();
    }

    public void parse(Iterable<Account> accounts){
        init();
        Iterator<Account>accountIterator=accounts.iterator();
        while (accountIterator.hasNext()){
            Account account=accountIterator.next();
            this.accounts.add(new AccountAdapter(account));
        }
    }

    public List<AccountAdapter> getAccounts() {
        return accounts;
    }

    public void setAccounts(Iterable<Account> accounts) {
        parse(accounts);
    }
}
