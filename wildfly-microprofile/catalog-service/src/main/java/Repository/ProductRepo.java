package Repository;

import Models.Category;
import Models.DTO.ProductDto;
import Models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductRepo {

    @PersistenceContext
    private EntityManager em;

    public List<ProductDto> getProducts() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList()
                .stream()
                .map(p -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setAvailability(p.getAvailability());
                    dto.setCategoryName(p.getCategoryName());
                    if (p.getCategory() != null) {
                        dto.setCategoryId(p.getCategory().getId());
                    }
                    return dto;
                })
                .toList();
    }

    public Product getProductsbyId(String id) {
        return em.createQuery("SELECT p FROM Product p WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    public List<ProductDto> getProductsbyCategory(String categoryId) {
        return em.createQuery("SELECT p FROM Product p WHERE p.category.id = :categoryId", Product.class)
                .setParameter("categoryId", categoryId)
                .getResultList()
                .stream()
                .map(p -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setAvailability(p.getAvailability());
                    if (p.getCategory() != null) {
                        dto.setCategoryId(p.getCategory().getId());
                    }
                    return dto;
                })
                .toList();
    }



    public int addProduct(ProductDto dto) {
        try {
            Product product = new Product();
            product.setId(dto.getId());
            product.setName(dto.getName());
            product.setAvailability(dto.getAvailability());
            product.setCategoryName(dto.getCategoryName());

            if (dto.getCategoryId() != null) {
                Category category = em.getReference(Category.class, dto.getCategoryId());
                product.setCategory(category);
            }

            em.persist(product);
            return 0;
        } catch (PersistenceException e) {
            return 1;
        }
    }

    public List<ProductDto> getProductsByName(String name) {
        return em.createQuery("SELECT p FROM Product p WHERE p.name = :Name", Product.class)
                .setParameter("Name", name)
                .getResultList()
                .stream()
                .map(p -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setAvailability(p.getAvailability());
                    if (p.getCategory() != null) {
                        dto.setCategoryId(p.getCategory().getId());
                    }
                    return dto;
                })
                .toList();
    }
}
