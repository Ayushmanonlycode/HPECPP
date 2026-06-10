package Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Item {

    @Id
    private String id;

    private String sku;

    private String itemName;

    @ManyToOne
    @JoinColumn(name = "product_id") //So, product_id is a foreign key in the Item table
    private Product product;

    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
}