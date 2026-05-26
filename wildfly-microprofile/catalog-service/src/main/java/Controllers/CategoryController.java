package Controllers;

import Models.Category;
import Repository.CategoryRepo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/category")
public class CategoryController {

    @Inject
    private CategoryRepo categoryRepo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public List<Category> getCategories() {
        return categoryRepo.getCategories();

    }

    @POST
    @Produces("text/plain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public String addCategory(Category category) {
        int res= categoryRepo.addCategory(category);

        if(res==0){
            return "Category Added Successfully!!!";

        }
        return "Category NOT Added";
    }
}
