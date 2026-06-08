package Repository;


import Models.Dto.OrderDto;
import Models.Orders;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OrderCaptureRepo {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public int placeOrder(Orders order) {
        try{
            em.persist(order);
            return 0;

        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
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

    public Orders getByOid(String oid) {
        return em.createQuery(
                        "SELECT o FROM Orders o WHERE o.orderId = :orderId",
                        Orders.class
                )
                .setParameter("orderId", oid).getSingleResult();
    }
}
