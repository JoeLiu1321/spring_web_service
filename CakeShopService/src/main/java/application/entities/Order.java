package application.entities;

import javax.persistence.*;

@Entity
@Table(name = "Record")
public class Order {
    @Id
    @Column(name="orderId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="productId")
    private Product product;
    private String accountName;
    private String time;
    private String action;

    public Order(){}
    public Order(Product product, String accountName, String time, String action) {
        this.product = product;
        this.accountName = accountName;
        this.time = time;
        this.action = action;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Product getProduct() {
        return product;
    }

    public String getTime() {
        return time;
    }

    public String getAction() {
        return action;
    }
}
