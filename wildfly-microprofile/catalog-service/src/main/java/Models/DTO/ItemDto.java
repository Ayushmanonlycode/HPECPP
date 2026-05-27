package Models.DTO;

public class ItemDto {
    private String id;
    private String itemName;
    private String productId; // just the ID, same pattern as ProductDto

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}