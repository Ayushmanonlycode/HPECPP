package com.showdown.wildfly.orderfulfillmentservice.Repository;

import com.showdown.wildfly.orderfulfillmentservice.Models.OrderFulfillment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class OrderFulfillmentRepository {

    @PersistenceContext(unitName = "fulfillmentPU")
    EntityManager em;

    public void addOrder(OrderFulfillment order) {
        em.persist(order);
    }

    public List<OrderFulfillment> getAllOrders() {
        return em.createQuery(
                "SELECT o FROM OrderFulfillment o",
                OrderFulfillment.class
        ).getResultList();
    }
    
    public OrderFulfillment getOrderById(String orderId) {
        return em.find(OrderFulfillment.class, orderId);
    }

    public OrderFulfillment updateOrder(OrderFulfillment order) {
        return em.merge(order);
    }

    public void deleteOrder(String orderId) {
        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null) {
            em.remove(order);
        }
    }
    public List<OrderFulfillment> getOrdersByStatus(String status) {
        return em.createQuery(
                "SELECT o FROM OrderFulfillment o WHERE o.status = :status",
                OrderFulfillment.class)
                .setParameter("status", status)
                .getResultList();
    }
    public void shipOrder(String orderId) {

        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null &&
            "PROCESSING".equals(order.getStatus())) {

            order.setStatus("SHIPPED");
            em.merge(order);
        }
    }

    public void deliverOrder(String orderId) {

        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null &&
            "SHIPPED".equals(order.getStatus())) {

            order.setStatus("DELIVERED");
            em.merge(order);
        }
    }

    public void cancelOrder(String orderId) {

        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null &&
            "PENDING".equals(order.getStatus())) {

            order.setStatus("CANCELLED");
            em.merge(order);
        }
    }
    public void processOrder(String orderId) {

        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null) {
            order.setStatus("PROCESSING");
            em.merge(order);
        }
    }
    public void failOrder(String orderId) {

        OrderFulfillment order =
                em.find(OrderFulfillment.class, orderId);

        if (order != null &&
            "PROCESSING".equals(order.getStatus())) {

            order.setStatus("FAILED");
            em.merge(order);
        }
    }
    public OrderFulfillment getByTrackingNumber(String trackingNumber) {

        try {
            return em.createQuery(
                    "SELECT o FROM OrderFulfillment o WHERE o.trackingNumber = :trackingNumber",
                    OrderFulfillment.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }
        public long countOrders() {
        return em.createQuery(
                "SELECT COUNT(o) FROM OrderFulfillment o",
                Long.class)
                .getSingleResult();
    }
    public double getTotalRevenue() {

        Double revenue = em.createQuery(
                "SELECT COALESCE(SUM(o.totalAmount),0) FROM OrderFulfillment o",
                Double.class)
                .getSingleResult();

        return revenue;
    }
}