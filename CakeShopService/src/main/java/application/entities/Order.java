package application.entities;

import javax.persistence.*;

@Entity
@Table(name = "Record")
public class Order {
    @Id
    @Column(name="orderId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    @JoinColumn(name="productId")
    private Product product;
    private Integer number;
    private String accountName;
    private String time;
    private Boolean isPay;
    private Boolean isFinish;

    public Order(){
        setPay(false);
        setFinish(false);
    }
    public Order(Product product, Integer number, String accountName, String time) {
        this();
        setProduct(product);
        setNumber(number);
        setAccountName(accountName);
        setTime(time);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
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

}
