package Service;


import Models.Item;
import Repository.ItemRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemService {

    @Inject
    ItemRepo itemRepo;

    public List<Item> getItems() {
        return itemRepo.getItems();
    }


    public int addItem(Item item){
        return itemRepo.addItem(item);
    }

    public List<Item> getItemsById(String id) {
        return itemRepo.getItemsById(id);
    }

    public List<Item> getItemsByProductId(String pid) {
        return itemRepo.getItemsByProductId(pid);
    }
}
