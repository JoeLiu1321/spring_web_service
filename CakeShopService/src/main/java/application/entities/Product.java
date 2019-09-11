package application.entities;


import javax.persistence.*;

@Entity
public class Product {
    @Id
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

    public void setProduct(Product product){
        this.name=product.name;
        this.description = product.description;
        this.onSell = product.onSell;
        this.price = product.price;
        this.volume = product.volume;
    }



    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
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
