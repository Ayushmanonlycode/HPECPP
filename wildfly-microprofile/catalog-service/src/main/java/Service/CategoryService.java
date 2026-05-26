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

    public List<Category> getAllCategories() {
        return categoryRepo.getCategories();
    }

    public int addCategory(Category category) {
        return categoryRepo.addCategory(category);
    }

}
