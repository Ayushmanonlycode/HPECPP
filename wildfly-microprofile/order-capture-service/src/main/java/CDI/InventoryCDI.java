package CDI;

import Client.InventoryClient;
import Client.UserClient;
import Models.Dto.InventoryDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class InventoryCDI {
    @Inject
    @RestClient
    InventoryClient inventoryClient;

    @Retry(maxRetries = 3)
    @Timeout(2000)
    @CircuitBreaker
    @Fallback(fallbackMethod = "inventoryFallback")
    public InventoryDto getInventory(String sku) {
        return inventoryClient.getInventoryBySku(sku);
    }

    public InventoryDto inventoryFallback(String sku) {
        throw new RuntimeException(
                "Unable to retrieve inventory for item " + sku +
                        ". Inventory service is currently unavailable."
        );
    }


    @Retry(maxRetries = 3)
    @Timeout(2000)
    @CircuitBreaker
    @Fallback(fallbackMethod = "updateInventoryFallback")
    public void updateInventory(String sku, int quantity) {
        inventoryClient.updateInventory(sku, quantity);
    }

    public void updateInventoryFallback(String sku, int quantity) {
        throw new RuntimeException(
                "Unable to update inventory."
        );
    }
}
