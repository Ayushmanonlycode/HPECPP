package Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Inventory {
    @Id
    private String id;
    private String itemId;
    private String itemSku;
    private int quantity;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemSku() {
        return itemSku;
    }
    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}