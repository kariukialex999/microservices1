package org.kariioke.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryItemResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private boolean inStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
