package Repository;


import Models.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ItemRepo {

    @PersistenceContext
    private EntityManager em;

    public List<Item> getItems() {
        return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
    }


    @Transactional
    public int addItem(Item item){
        em.persist(item);
        return 0;
    }

    public List<Item> getItemsById(String id) {
        return em.createQuery("SELECT i FROM Item i WHERE i.id= :id", Item.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Item> getItemsByProductId(String pid) {
        return em.createQuery("SELECT i FROM Item i WHERE i.productId= :pid", Item.class)
                .setParameter("pid", pid)
                .getResultList();
    }
}
