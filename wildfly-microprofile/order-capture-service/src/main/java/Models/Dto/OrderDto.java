package Models.Dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderDto {

    private UUID id;
    private String userId;
    private List<LineItemDto> lineItems;
    private String shippingAddress;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<LineItemDto> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemDto> lineItems) { this.lineItems = lineItems; }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}