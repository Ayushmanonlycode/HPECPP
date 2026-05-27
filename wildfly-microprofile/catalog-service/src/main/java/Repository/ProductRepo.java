package Repository;

import Models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public List<Product> getProductsbyCategory(String category){
        return em.createQuery("SELECT p FROM Product p WHERE p.category= :category", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Transactional
    public int addProduct(Product product) {
        em.persist(product);

        return 0;
    }
}
