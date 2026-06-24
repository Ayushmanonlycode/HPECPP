package Models.DTO;

import java.math.BigDecimal;

public class ItemDto {
    private String id;
    private String sku;
    private String itemName;
    private String productId;
    private BigDecimal listPrice;
    private String imageUrl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}