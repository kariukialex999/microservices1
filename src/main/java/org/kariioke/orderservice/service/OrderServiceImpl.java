package org.kariioke.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.kariioke.orderservice.dto.InventoryItemResponse;
import org.kariioke.orderservice.dto.OrderRequest;
import org.kariioke.orderservice.dto.OrderResponse;
import org.kariioke.orderservice.exceptions.InvalidOrderOperationException;
import org.kariioke.orderservice.exceptions.OrderNotFoundException;
import org.kariioke.orderservice.feignClient.InventoryClient;
import org.kariioke.orderservice.model.Order;
import org.kariioke.orderservice.model.OrderStatus;
import org.kariioke.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;


    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        InventoryItemResponse item = inventoryClient.getItemById(request.getInventoryItemId());
        inventoryClient.reduceStock(request.getInventoryItemId(), request.getQuantity());
        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .inventoryItemId(item.getId())
                .itemName(item.getName())
                .itemCategory(item.getCategory())
                .quantity(request.getQuantity())
                .unitPrice(item.getPrice())
                .totalPrice(totalPrice)
                .status(OrderStatus.CONFIRMED)
                .build();
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = findOrThrow(id);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderOperationException("Order " + id + " is already cancelled");
        }

        // Restore stock in inventory-service
        inventoryClient.restoreStock(order.getInventoryItemId(), order.getQuantity());

        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse getOrderById(Long id) {
        return toResponse(findOrThrow(id));
    }

    private Order findOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .inventoryItemId(order.getInventoryItemId())
                .itemName(order.getItemName())
                .itemCategory(order.getItemCategory())
                .quantity(order.getQuantity())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
