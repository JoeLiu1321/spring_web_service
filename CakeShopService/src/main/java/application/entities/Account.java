package application.entities;


import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String account;
    private String password;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "privilegeId")
    private Privilege privilege;
    public Account(){

    }

    public Account(String account, String password) {
        this.account = account;
        this.password = password;
        setPrivilege(new Privilege(0,"inValid"));
    }

    public String getPrivilege() {
        return privilege.getPrililege();
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public String getAccount() {
        return account;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
