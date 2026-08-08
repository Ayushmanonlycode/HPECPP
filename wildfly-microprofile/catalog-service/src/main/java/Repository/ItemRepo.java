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

    public List<ItemDto> getItems() {
        return em.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList()
                .stream()
                .map(i -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(i.getId());
                    dto.setItemName(i.getItemName());
                    dto.setListPrice(i.getListPrice());
                    dto.setSku(i.getSku());
                    dto.setImageUrl(i.getImageUrl());
                    if (i.getProduct() != null) {
                        dto.setProductId(i.getProduct().getId());
                    }
                    return dto;
                })
                .toList();
    }


    @Transactional
    public int addItem(ItemDto dto) {
        try {
            Item item = new Item();
            item.setId(dto.getId());
            item.setItemName(dto.getItemName());
            item.setListPrice(dto.getListPrice());
            item.setSku(dto.getSku());
            item.setImageUrl(dto.getImageUrl());

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

    public List<Item> getItemsById(String productId) {
        return em.createQuery(
                        "SELECT i FROM Item i WHERE i.product.id = :productId",
                        Item.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    public List<ItemDto> getItemsByProductId(String pid) {
        return em.createQuery("SELECT i FROM Item i WHERE i.product.id = :pid", Item.class)
                .setParameter("pid", pid)
                .getResultList()
                .stream()
                .map(i -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(i.getId());
                    dto.setItemName(i.getItemName());
                    dto.setListPrice(i.getListPrice());
                    dto.setImageUrl(i.getImageUrl());
                    dto.setSku(i.getSku());
                    if (i.getProduct() != null) {
                        dto.setProductId(i.getProduct().getId());
                    }
                    return dto;
                })
                .toList();
    }

    public Item getItemsBySku(String sku) {
        return em.createQuery("SELECT i FROM Item i WHERE i.sku = :SKU", Item.class)
                .setParameter("SKU", sku)
                .getSingleResult();
    }
}
