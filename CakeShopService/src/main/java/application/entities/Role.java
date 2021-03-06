package application.entities;

import application.RoleEnum;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Optional;

//  id : 0  ,  role : no permission
//  id : 1  ,  role : user
//  id : 2  ,  role : employee
//  id : 3  ,  role : admin
@Entity
public class Role {
    @Id
    private Integer id;
    private String role;

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public Role(){
        this(RoleEnum.NO_PERMISSION);
    }

    public Role(@NotNull RoleEnum roleEnum){
        setId(roleEnum.ordinal());
        setRole(roleEnum.name());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private boolean isEqual(Role role){
        return isEqual(role.getId(),role.getRole());
    }

    public boolean isEqual(RoleEnum roleEnum){
        return isEqual(roleEnum.ordinal(),roleEnum.name());
    }

    private boolean isEqual(Integer id, String role){
        boolean isIdEqual=this.id.equals(id),isRoleEqual=this.role.equals(role);
        return isIdEqual && isRoleEqual;
    }

    @Override
    public boolean equals(Object role){
        Optional<Role> optionalRole=Optional.ofNullable((Role)role);
        return optionalRole.isPresent() && isEqual(optionalRole.get());
    }
}
