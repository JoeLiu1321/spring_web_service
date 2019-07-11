package application.entities;


import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String accountName;
    private String password;
    @Column(columnDefinition = "integer default 1")
    private Integer privilege;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountId")
    private AccountInfo info;
    public Account(){

    }

    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public void setAccount(Account account){
        setPassword(account.getPassword());
        setPrivilege(account.getPrivilege());
        setInfo(account.getInfo());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
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
