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


    public List<Product> getProductsbyId(String id){
        return productRepo.getProductsbyId(id);
    }

    public List<Product> getProductsByCategory(String category){
        return productRepo.getProductsbyCategory(category);
    }


}
