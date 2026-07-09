package Repository;


import Models.Inventory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class InventoryRepo {

    @PersistenceContext
    private EntityManager em;



    public List<Inventory> getAllInventory() {
        return em.createQuery("select i from Inventory i", Inventory.class).getResultList();
    }


    @Transactional

    public int addInventory(Inventory inventory) {
        try {

            em.persist(inventory);

            em.flush();

            return 0;

        } catch (PersistenceException e) {

            return 1;
        }

    }

    public List<Inventory> getInventoryByItemId(String itemId) {
        return em.createQuery("SELECT i FROM Inventory i WHERE i.itemId= :itemId", Inventory.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }

    @Transactional
    public int updateInventoryByItemId(String itemId, int quantity) {
        List<Inventory> result = em.createQuery(
                        "SELECT i FROM Inventory i WHERE i.itemId = :itemId",
                        Inventory.class)
                .setParameter("itemId", itemId)
                .getResultList();
        if (result.isEmpty()) {
            return 1;
        }
        Inventory inventory = result.get(0);
        inventory.setQuantity(quantity);
        em.merge(inventory);
        return 0;
    }
    public Inventory getInventoryByItemSku(String sku) {
        try {
            return em.createQuery("SELECT i FROM Inventory i WHERE i.itemSku= :sku", Inventory.class)
                    .setParameter("sku", sku)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Transactional
    public int updateInventoryBySku(String sku, int quantity) {
        List<Inventory> result = em.createQuery(
                        "SELECT i FROM Inventory i WHERE i.itemSku = :sku",
                        Inventory.class)
                .setParameter("sku", sku)
                .getResultList();

        if (result.isEmpty()) {
            return 1;
        }

        Inventory inventory = result.get(0);

        inventory.setQuantity(quantity);

        em.merge(inventory);

        return 0;
    }
}
