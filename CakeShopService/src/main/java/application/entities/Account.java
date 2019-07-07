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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "privilegeId")
    private Privilege privilege;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountId")
    private AccountInfo info;
    public Account(){

    }

    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public String getPrivilege() {
        return privilege.getPrililege();
    }

    public void setPrivilege(Privilege privilege) {
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
