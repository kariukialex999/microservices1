package org.kariioke.orderservice.feignClient;

import org.kariioke.orderservice.dto.InventoryItemResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/{id}")
    InventoryItemResponse getItemById(@PathVariable("id") Long id);

    @PatchMapping("/inventory/{id}/reduce")
    InventoryItemResponse reduceStock(@PathVariable("id") Long id, @RequestParam int quantity);

    @PatchMapping("/inventory/{id}/restore")
    InventoryItemResponse restoreStock(@PathVariable("id") Long id, @RequestParam int quantity);
}
