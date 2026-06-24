package Service;

import Models.Category;
import Models.DTO.CategoryDto;
import Repository.CategoryRepo;
import Repository.ProductRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepo categoryRepo;

    public List<CategoryDto> getCategories() {
        return categoryRepo.getCategories();
    }

    public int addCategory(CategoryDto category) {
        return categoryRepo.addCategory(category);
    }

    public CategoryDto getCategoriesById(String id) {

        Category c= categoryRepo.getCategoriesById(id);

        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setCategoryName(c.getCategoryName());
        return dto;
    }
}
