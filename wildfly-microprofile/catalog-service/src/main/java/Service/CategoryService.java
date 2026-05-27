package Service;

import Models.Category;
import Repository.CategoryRepo;
import Repository.ProductRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepo categoryRepo;

    public List<Category> getCategories() {
        return categoryRepo.getCategories();
    }

    public int addCategory(Category category) {
        return categoryRepo.addCategory(category);
    }

    public List<Category> getCategoriesById(String id) {
        return categoryRepo.getCategoriesById(id);
    }
}
