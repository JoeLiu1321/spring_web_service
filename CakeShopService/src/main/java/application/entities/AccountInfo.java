package application.entities;

import javax.persistence.*;

@Entity
public class AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 10)
    private String identify;
    @Column(length = 10)
    private String name;
    @Column(length = 11)
    private String phone;
    @Column(length = 20)
    private String address;
    @Column(length = 25)
    private String email;
    @ManyToOne
    @JoinColumn(name="inviteAccount")
    private Account inviteAccount;
    @ManyToOne
    @JoinColumn(name="roleId")
    private Role role;

    public AccountInfo(){
        identify="";
        name="";
        phone="";
        address="";
        email="";
        role=new Role();
    }

    public AccountInfo(String name, String phone, String address, String email) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public Account getInviteAccount() {
        return inviteAccount;
    }

    public void setInviteAccount(Account inviteAccount) {
        this.inviteAccount = inviteAccount;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}