package Repository;


import Models.Dto.OrderDto;
import Models.Orders;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderCaptureRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Orders placeOrder(Orders order) {
        try{
            em.persist(order);
            return order;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<Orders> getAllOrders() {
        return em.createQuery("SELECT o FROM Orders o", Orders.class)
                .getResultList();
    }

    public List<Orders> getByCid(String cid) {
        return em.createQuery(
                        "SELECT o FROM Orders o WHERE o.customerId = :userId",
                        Orders.class
                )
                .setParameter("userId", cid)
                .getResultList();

    }

    public Orders getByOid(UUID oid) {
        return em.createQuery(
                        "SELECT o FROM Orders o WHERE o.id = :orderId",
                        Orders.class
                )
                .setParameter("orderId", oid).getSingleResult();
    }
}
