package Service;

import Models.DTO.ProductDto;
import Models.Product;
import Repository.ProductRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepo productRepo;

    public List<ProductDto> getAllProducts() {
        return productRepo.getProducts();
    }

    @Transactional
    public int addProduct(ProductDto product){
        return productRepo.addProduct(product);
    }


    public ProductDto getProductsbyId(String id){
        Product p=  productRepo.getProductsbyId(id);

        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setAvailability(p.getAvailability());
        dto.setCategoryName(p.getCategoryName());
        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
        }

        return dto;
    }

    public List<ProductDto> getProductsByCategory(String category){
        return productRepo.getProductsbyCategory(category);
    }


    public List<ProductDto> getProductsByName(String name) {
        return productRepo.getProductsByName(name);
    }
}
