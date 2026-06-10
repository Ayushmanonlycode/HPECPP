package Controllers;

import Models.DTO.ProductDto;
import Models.Product;
import Service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("products")
public class ProductController {

    @Inject
    ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDto> getProducts(@QueryParam("categoryId") String category){
        if (category == null) {
            return productService.getAllProducts();
        }

        return productService.getProductsByCategory(category);

    }

    @POST
    @Path("/add")
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(ProductDto product){
        int res=productService.addProduct(product);

        if(res==0){
            return Response.ok("Product Added Successfully!!!").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductDto getProductsbyId(@PathParam("id") String id){
        return productService.getProductsbyId(id);

    }





    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDto> getProductsbyName(@QueryParam("q") String name){
        return productService.getProductsByName(name);

    }


}
