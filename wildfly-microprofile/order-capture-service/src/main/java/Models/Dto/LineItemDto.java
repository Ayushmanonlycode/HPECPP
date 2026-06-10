package Models.Dto;

import java.math.BigDecimal;

public class LineItemDto {

    private String itemSku;
    private int quantity;
    private BigDecimal unitPrice;

    public String getItemSku() { return itemSku; }
    public void setItemSku(String itemId) { this.itemSku = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}