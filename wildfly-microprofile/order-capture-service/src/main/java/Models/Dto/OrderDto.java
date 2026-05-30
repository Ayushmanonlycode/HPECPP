package Models.Dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto {

    private String customerId;
    private List<LineItemDto> lineItems;


    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<LineItemDto> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemDto> lineItems) { this.lineItems = lineItems; }
}