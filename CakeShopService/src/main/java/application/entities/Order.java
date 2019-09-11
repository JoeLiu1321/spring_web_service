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
    @ManyToOne
    @JoinColumn(name="paymentId")
    private Payment payment;
    private Integer number;
    private String accountName;
    private String time;
    private boolean isPay;

    public Order(){
        setPayment(null);
        setPay(false);
    }
    public Order(Product product, Integer number, String accountName, String time) {
        this();
        setProduct(product);
        setNumber(number);
        setAccountName(accountName);
        setTime(time);
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
