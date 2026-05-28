package Controllers;

import Models.DTO.ProductDto;
import Models.Product;
import Service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("product")
public class ProductController {

    @Inject
    ProductService productService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDto> getProducts(){
        return productService.getAllProducts();

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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDto> getProductsbyId(@QueryParam("id") String id){
        return productService.getProductsbyId(id);

    }


    @GET
    @Path("/search/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDto> getProductsbyCategory(@QueryParam("cat") String category){
        return productService.getProductsByCategory(category);

    }



}
