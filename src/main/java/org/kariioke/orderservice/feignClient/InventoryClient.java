package org.kariioke.orderservice.feignClient;

import org.kariioke.orderservice.dto.InventoryItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/{id}")
    InventoryItemResponse getItemById(@PathVariable("id") Long id);

    @PutMapping("/inventory/{id}/reduce")
    InventoryItemResponse reduceStock(@PathVariable("id") Long id, @RequestParam int quantity);

    @PutMapping("/inventory/{id}/restore")
    InventoryItemResponse restoreStock(@PathVariable("id") Long id, @RequestParam int quantity);
}
