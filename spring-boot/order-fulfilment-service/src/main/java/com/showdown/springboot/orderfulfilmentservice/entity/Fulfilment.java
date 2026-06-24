package com.showdown.springboot.orderfulfilmentservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fulfilments")
public class Fulfilment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfilmentStatus status = FulfilmentStatus.PENDING;

    private String trackingNumber;
    private String carrier;
    private Instant shippedAt;
    private Instant deliveredAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Fulfilment() {
    }

    // ── Getters and Setters ──────────────────────────────────────

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public FulfilmentStatus getStatus() { return status; }
    public void setStatus(FulfilmentStatus status) { this.status = status; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public Instant getShippedAt() { return shippedAt; }
    public void setShippedAt(Instant shippedAt) { this.shippedAt = shippedAt; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
