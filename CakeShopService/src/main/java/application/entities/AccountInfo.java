package application.entities;

import javax.persistence.*;

@Entity
public class AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String identify;
    private String name;
    private String phone;
    private String address;
    private String email;

    public AccountInfo(){
        identify="";
        name="";
        phone="";
        address="";
        email="";
    }

    public AccountInfo(String name, String phone, String address, String email) {
        this();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
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