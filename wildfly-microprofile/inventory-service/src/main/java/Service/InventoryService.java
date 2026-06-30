package Service;


import Models.Inventory;
import Repository.InventoryRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class InventoryService {

    @Inject
    InventoryRepo inventoryRepo;


    public List<Inventory> getAllInventory() {
        return inventoryRepo.getAllInventory();
    }

    public int addInventory(Inventory inventory) {
        return inventoryRepo.addInventory(inventory);
    }

    public List<Inventory> getInventoryByItemId(String itemId) {
        return inventoryRepo.getInventoryByItemId(itemId);
    }

    public int updateInventory(String itemId, int quantity) {
        return inventoryRepo.updateInventory(itemId, quantity);
    }
}
