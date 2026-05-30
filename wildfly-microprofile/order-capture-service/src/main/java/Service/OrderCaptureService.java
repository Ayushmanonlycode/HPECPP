package Service;

import Models.Dto.LineItemDto;
import Models.Dto.OrderDto;
import Models.LineItem;
import Models.Orders;
import Repository.OrderCaptureRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;

@ApplicationScoped
public class OrderCaptureService {

    @Inject
    OrderCaptureRepo orderRepo;

    public int placeOrder(OrderDto orderDto) {

        Orders order = new Orders();

        order.setCustomerId(orderDto.getCustomerId());

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(orderDto.getLineItems() == null){
            return 1;
        }

        for(LineItemDto itemDto : orderDto.getLineItems()) {

            BigDecimal price = itemDto.getUnitPrice();
            int quantity = itemDto.getQuantity();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            totalAmount = totalAmount.add(itemTotal);

            LineItem lineItem = new LineItem();

            lineItem.setItemId(itemDto.getItemId());
            lineItem.setQuantity(quantity);
            lineItem.setUnitPrice(price);

            order.addLineItem(lineItem);
        }

        order.setTotalAmount(totalAmount);

        return orderRepo.placeOrder(order);
    }
}