package Service;


import Models.DTO.ItemDto;
import Models.Item;
import Repository.ItemRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
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
        List<Item> items = itemRepo.getItemsById(id);

        List<ItemDto> dto= new ArrayList<>();

        for (Item i : items) {

            ItemDto idto = new ItemDto();
            idto.setId(i.getId());
            idto.setItemName(i.getItemName());
            idto.setListPrice(i.getListPrice());
            idto.setSku(i.getSku());
            idto.setImageUrl(i.getImageUrl());
            if (i.getProduct() != null) {
                idto.setProductId(i.getProduct().getId());
            }
            dto.add(idto);

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
        dto.setListPrice(i.getListPrice());
        dto.setSku(i.getSku());
        dto.setImageUrl(i.getImageUrl());
        if (i.getProduct() != null) {
            dto.setProductId(i.getProduct().getId());
        }
        return dto;
    }
}
