package Service;


import Models.DTO.ItemDto;
import Models.Item;
import Repository.ItemRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemService {

    @Inject
    ItemRepo itemRepo;

    public List<ItemDto> getItems() {
        return itemRepo.getItems();
    }


    public int addItem(ItemDto item){
        return itemRepo.addItem(item);
    }

    public List<ItemDto> getItemsById(String id) {
        return itemRepo.getItemsById(id);
    }

    public List<ItemDto> getItemsByProductId(String pid) {
        return itemRepo.getItemsByProductId(pid);
    }
}
