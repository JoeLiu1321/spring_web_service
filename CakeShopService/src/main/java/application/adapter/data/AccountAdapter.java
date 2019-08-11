package application.adapter.data;

import application.entities.Account;
import application.entities.AccountInfo;
import application.entities.Role;

import java.util.Optional;

public class AccountAdapter implements DataAdapter {
    private String accountName;
    private Role role;
    private String identify;
    private String name;
    private String phone;
    private String address;
    private String email;
    private AccountAdapter inviteAccount;

    public AccountAdapter(Account account) {
        parse(account);
    }

    public void parse(Account account){
        AccountInfo accountInfo=account.getInfo();
        accountName=account.getAccountName();
        role =accountInfo.getRole();
        boolean hasInviteAccount=Optional.ofNullable(accountInfo.getInviteAccount()).isPresent();
        if(hasInviteAccount)
            inviteAccount=new AccountAdapter(accountInfo.getInviteAccount());
        identify=accountInfo.getIdentify();
        name=accountInfo.getName();
        phone=accountInfo.getPhone();
        address=accountInfo.getAddress();
        email=accountInfo.getEmail();
    }


    public AccountAdapter getInviteAccount() {
        return inviteAccount;
    }

    public void setInviteAccount(AccountAdapter inviteAccount) {
        this.inviteAccount = inviteAccount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
