package Service;

import CDI.InventoryCDI;
import CDI.UserCDI;
import Models.Dto.InventoryDto;
import Models.Dto.LineItemDto;
import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Models.LineItem;
import Models.Orders;
import Repository.OrderCaptureRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderCaptureService {

    @Inject
    OrderCaptureRepo orderRepo;



    @Inject
    InventoryCDI inventoryCDI;

    @Inject
    UserCDI userCDI;


    @Retry(maxRetries = 3)
    @Timeout(3000)
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 5000
    )
    @Fallback(fallbackMethod = "createOrderFallback")
    public Orders placeOrder(OrderDto orderDto) {

        Orders order = new Orders();

        Response response =
                userCDI.checkUser(
                        orderDto.getUserId()
                );

        if (response.getStatus() != 200) {
            throw new RuntimeException(
                    "User does not exist"
            );
        }

        order.setCustomerId(orderDto.getUserId());
        order.setShippingAddress(orderDto.getShippingAddress());

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(orderDto.getLineItems() == null){
            return null;
        }

        for(LineItemDto itemDto : orderDto.getLineItems()) {

            InventoryDto inv= inventoryCDI.getInventory(itemDto.getItemSku());

            if (inv == null) {
                throw new RuntimeException(
                        "Item not found in inventory: " + itemDto.getItemSku());
            }

            BigDecimal price = itemDto.getUnitPrice();
            int quantity = itemDto.getQuantity();

            if(inv.getQuantity() < quantity){
                throw new RuntimeException(
                        "Insufficient inventory for item "
                                + itemDto.getItemSku());

            }

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            totalAmount = totalAmount.add(itemTotal);

            LineItem lineItem = new LineItem();

            lineItem.setItemSku(itemDto.getItemSku());
            lineItem.setQuantity(quantity);
            lineItem.setUnitPrice(price);

            order.addLineItem(lineItem);

            int newQuantity = inv.getQuantity() - quantity;

            inventoryCDI.updateInventory(
                    itemDto.getItemSku(),
                    newQuantity
            );
        }

        order.setTotalAmount(totalAmount);

        return orderRepo.placeOrder(order);
    }

    public Orders createOrderFallback(OrderDto orderDto) {
        throw new RuntimeException("Order service temporarily unavailable.");
    }

    @Transactional
    public List<OrderResponseDto> getAllOrders() {
        List<Orders> orders=  orderRepo.getAllOrders();

        return orders.stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();

            dto.setId(order.getId());
            dto.setCustomerId(order.getCustomerId());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setStatus(order.getStatus());
            dto.setShippingAddress(order.getShippingAddress());

            List<LineItemDto> lineItemDtos =
                    order.getLineItems()
                            .stream()
                            .map(item -> {

                                LineItemDto itemDto = new LineItemDto();

                                itemDto.setItemSku(item.getItemSku());
                                itemDto.setQuantity(item.getQuantity());
                                itemDto.setUnitPrice(item.getUnitPrice());

                                return itemDto;
                            })
                            .toList();

            dto.setLineItems(lineItemDtos);

            return dto;
        }).toList();
    }



    @Transactional
    public List<OrderResponseDto> getByCid(String cid) {
        List<Orders> orders= orderRepo.getByCid(cid);


        return orders.stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();

            dto.setId(order.getId());
            dto.setCustomerId(order.getCustomerId());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setStatus(order.getStatus());
            dto.setShippingAddress(order.getShippingAddress());

            List<LineItemDto> lineItemDtos =
                    order.getLineItems()
                            .stream()
                            .map(item -> {

                                LineItemDto itemDto = new LineItemDto();

                                itemDto.setItemSku(item.getItemSku());
                                itemDto.setQuantity(item.getQuantity());
                                itemDto.setUnitPrice(item.getUnitPrice());

                                return itemDto;
                            })
                            .toList();

            dto.setLineItems(lineItemDtos);

            return dto;
        }).toList();
    }


    @Transactional
    public OrderResponseDto getByOid(UUID oid) {
        Orders order= orderRepo.getByOid(oid);

        OrderResponseDto dto = new OrderResponseDto();

        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());

        List<LineItemDto> lineItemDtos =
                order.getLineItems()
                        .stream()
                        .map(item -> {

                            LineItemDto itemDto = new LineItemDto();

                            itemDto.setItemSku(item.getItemSku());
                            itemDto.setQuantity(item.getQuantity());
                            itemDto.setUnitPrice(item.getUnitPrice());

                            return itemDto;
                        })
                        .toList();

        dto.setLineItems(lineItemDtos);

        return dto;
    }
}