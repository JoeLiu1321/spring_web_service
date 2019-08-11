package application.entities;


import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true,length = 20)
    private String accountName;
    @Column(length = 15)
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountId")
    private AccountInfo info;
    public Account(){
        info=new AccountInfo();
    }

    public Account(String accountName, String password) {
        this();
        setAccountName(accountName);
        setPassword(password);
    }

    public void setAccount(Account account){
        setPassword(account.getPassword());
        setInfo(account.getInfo());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public AccountInfo getInfo() {
        return info;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setInfo(AccountInfo info) {
        this.info = info;
    }

    public String getPassword() {
        return password;
    }
}
