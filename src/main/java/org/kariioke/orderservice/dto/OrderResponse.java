package org.kariioke.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import org.kariioke.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long inventoryItemId;
    private String itemName;
    private String itemCategory;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
