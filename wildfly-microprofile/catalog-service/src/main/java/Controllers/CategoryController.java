package Controllers;

import Models.Category;
import Models.DTO.CategoryDto;
import Repository.CategoryRepo;
import Service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/category")
public class CategoryController {

    @Inject
    private CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public List<CategoryDto> getCategories() {
        return categoryService.getCategories();

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public List<Category> getCategories(@QueryParam("id") String id) {
        return categoryService.getCategoriesById(id);

    }




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response addCategory(Category category) {
        int res= categoryService.addCategory(category);

        if(res==0){
            return Response.ok("Category Added Successfully").build();

        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }



}
