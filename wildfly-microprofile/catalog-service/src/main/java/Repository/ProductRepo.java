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

    public List<Product> getProducts() {
        return em.createQuery(
                        "SELECT p FROM Product p", Product.class)
                .getResultList();

    };

    public List<Product> getProductsbyId(String id){
        return em.createQuery("SELECT p FROM Product p WHERE p.id = :id", Product.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Product> getProductsbyCategory(String categoryId){

        return em.createQuery(
                        "SELECT p FROM Product p WHERE p.category.id = :categoryId",
                        Product.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }



    public int addProduct(ProductDto dto) {
        try {
            Product product = new Product();
            product.setId(dto.getId());
            product.setName(dto.getName());
            product.setAvailability(dto.getAvailability());

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
}
