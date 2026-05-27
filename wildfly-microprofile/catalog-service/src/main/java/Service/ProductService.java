package Service;

import Models.Product;
import Repository.ProductRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.getProducts();
    }

    public int addProduct(Product product){
        return productRepo.addProduct(product);
    }


    public List<Product> getProductsbyId(String id){
        return productRepo.getProductsbyId(id);
    }

    public List<Product> getProductsByCategory(String category){
        return productRepo.getProductsbyCategory(category);
    }


}
