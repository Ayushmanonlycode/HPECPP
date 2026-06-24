package Service;

import Models.Dto.LineItemDto;
import Models.Dto.OrderDto;
import Models.Dto.OrderResponseDto;
import Models.LineItem;
import Models.Orders;
import Repository.OrderCaptureRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderCaptureService {

    @Inject
    OrderCaptureRepo orderRepo;

    public Orders placeOrder(OrderDto orderDto) {

        Orders order = new Orders();

        order.setCustomerId(orderDto.getUserId());
        order.setShippingAddress(orderDto.getShippingAddress());

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(orderDto.getLineItems() == null){
            return null;
        }

        for(LineItemDto itemDto : orderDto.getLineItems()) {

            BigDecimal price = itemDto.getUnitPrice();
            int quantity = itemDto.getQuantity();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            totalAmount = totalAmount.add(itemTotal);

            LineItem lineItem = new LineItem();

            lineItem.setItemSku(itemDto.getItemSku());
            lineItem.setQuantity(quantity);
            lineItem.setUnitPrice(price);

            order.addLineItem(lineItem);
        }

        order.setTotalAmount(totalAmount);

        return orderRepo.placeOrder(order);
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