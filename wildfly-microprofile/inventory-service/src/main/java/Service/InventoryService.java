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

    public int updateInventoryByItemId(String itemId, int quantity) {
        return inventoryRepo.updateInventoryByItemId(itemId, quantity);
    }

    public Inventory getInventoryByItemSku(String sku) {
        return inventoryRepo.getInventoryByItemSku(sku);
    }

    public int updateInventoryBySku(String sku, int quantity) {
        return inventoryRepo.updateInventoryBySku(sku, quantity);
    }
}