package Repository;

import Models.Category;
import Models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;

import java.util.List;

@ApplicationScoped
public class CategoryRepo {
    @PersistenceContext
    private EntityManager em;


    public List<Category> getCategories() {
        return em.createQuery(
                        "SELECT p FROM Category p", Category.class)
                .getResultList();
    }


    @Transactional
    public int addCategory(Category category) {
        try {

            em.persist(category);

            em.flush();

            return 0;

        } catch (PersistenceException e) {

            return 1;
        }
    }

    public List<Category> getCategoriesById(String id){
        return em.createQuery("SELECT p FROM Category p WHERE p.id= :id", Category.class)
                .setParameter("id", id)
                .getResultList();
    }
}
