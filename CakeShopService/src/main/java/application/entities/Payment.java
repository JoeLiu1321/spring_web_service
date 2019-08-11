package application.entities;

import org.springframework.data.repository.query.Param;

import javax.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String time;
    private Integer price;
    private Boolean isPay;

    public Payment(){

    }

    public Payment(String time, Integer price, Boolean isPay) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.isPay = isPay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }
}
