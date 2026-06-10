package Models.Dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {

    private String customerId;
    private List<LineItemDto> lineItems;
    private String address;


    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<LineItemDto> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemDto> lineItems) { this.lineItems = lineItems; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}