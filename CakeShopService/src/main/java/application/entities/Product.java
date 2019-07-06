package application.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @Column(name="productId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name="productName")
    private String name;
    private String description;
    private Boolean onSell;
    private Integer price;
    private Integer volume;

    public Product() {
        this.onSell=false;
        this.volume=0;
    }

    public Product(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.onSell = false;
        this.price = price;
        this.volume = 0;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getOnSell() {
        return onSell;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOnSell(Boolean onSell) {
        this.onSell = onSell;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
