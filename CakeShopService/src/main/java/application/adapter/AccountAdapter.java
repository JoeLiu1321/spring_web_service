package application.adapter;

import application.entities.Account;
import application.entities.AccountInfo;

public class AccountAdapter {
    private String accountName;
    private Integer privilege;
    private String identify;
    private String name;
    private String phone;
    private String address;
    private String email;


    public AccountAdapter(Account account) {
        parse(account);
    }

    public void parse(Account account){
        AccountInfo accountInfo=account.getInfo();

        accountName=account.getAccountName();
        privilege=account.getPrivilege();
        identify=accountInfo.getIdentify();
        name=accountInfo.getName();
        phone=accountInfo.getPhone();
        address=accountInfo.getAddress();
        email=accountInfo.getEmail();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
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
