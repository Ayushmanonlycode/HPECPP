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

    public ItemDto getItemsById(String id) {
        Item i = itemRepo.getItemsById(id);

        ItemDto dto = new ItemDto();
        dto.setId(i.getId());
        dto.setItemName(i.getItemName());
        dto.setPrice(i.getPrice());
        dto.setSku(i.getSku());
        if (i.getProduct() != null) {
            dto.setProductId(i.getProduct().getId());
        }
        return dto;


    }

    public List<ItemDto> getItemsByProductId(String pid) {
        return itemRepo.getItemsByProductId(pid);
    }

    public ItemDto getItemsBySku(String sku) {
        Item i=  itemRepo.getItemsBySku(sku);

        ItemDto dto = new ItemDto();
        dto.setId(i.getId());
        dto.setItemName(i.getItemName());
        dto.setPrice(i.getPrice());
        dto.setSku(i.getSku());
        if (i.getProduct() != null) {
            dto.setProductId(i.getProduct().getId());
        }
        return dto;
    }
}
