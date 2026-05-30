package Repository;


import Models.Orders;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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
}
