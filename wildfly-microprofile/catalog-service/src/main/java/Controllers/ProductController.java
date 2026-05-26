package Controllers;

import Models.Product;
import Service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("product")
public class ProductController {

    @Inject
    ProductService productService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts(){
        return productService.getAllProducts();

    }

    @POST
    @Path("/add")
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addProduct(Product product){
        int res=productService.addProduct(product);

        if(res==0){
            return "Product Added Successfully!!!";
        }

        return "Product NOT Added";

    }


    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProductsbyName(@QueryParam("name") String name){
        return productService.getProductsbyName(name);

    }



}
