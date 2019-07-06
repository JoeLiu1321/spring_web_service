package application.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Privilege {
    @Id
    @Column(name="privilegeId")
    private Integer id;
    private String privilege;
    @OneToMany(mappedBy = "privilege")
    private List<Account> account;
    public Privilege(Integer id, String privilege) {
        this.id = id;
        this.privilege = privilege;
    }

    public Privilege() {

    }

    public Integer getPrivilegeId() {
        return id;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.id = privilegeId;
    }

    public String getPrililege() {
        return privilege;
    }

    public void setPrililege(String prililege) {
        this.privilege = prililege;
    }

    public List<Account> getAccount() {
        return account;
    }
}
