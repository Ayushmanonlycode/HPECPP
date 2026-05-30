package Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ORDERID", length = 36)
    private String orderId;

    @Column(name = "USERID", nullable = false, length = 80)
    private String customerId;

    @Column(name = "TOTALPRICE", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "ORDERDATE")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<LineItem> lineItems = new ArrayList<>();

    public void addLineItem(LineItem item) {
        lineItems.add(item);
        item.setOrder(this);
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if(status == null) {
            status = OrderStatus.CREATED;
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
}