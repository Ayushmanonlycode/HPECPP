package Repository;


import Models.DTO.ItemDto;
import Models.Item;
import Models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
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
    public int addItem(ItemDto dto) {
        try {
            Item item = new Item();
            item.setId(dto.getId());
            item.setItemName(dto.getItemName());

            if (dto.getProductId() != null) {
                Product product = em.getReference(Product.class, dto.getProductId());
                item.setProduct(product);
            }

            em.persist(item);
            em.flush();
            return 0;

        } catch (PersistenceException e) {
            return 1;
        }
    }

    public List<Item> getItemsById(String id) {
        return em.createQuery("SELECT i FROM Item i WHERE i.id= :id", Item.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Item> getItemsByProductId(String pid) {
        return em.createQuery(
                        "SELECT i FROM Item i WHERE i.product.id = :pid",
                        Item.class)
                .setParameter("pid", pid)
                .getResultList();
    }
}
