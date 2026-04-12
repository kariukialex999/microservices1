package org.kariioke.orderservice.service;

import org.kariioke.orderservice.dto.OrderRequest;
import org.kariioke.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    OrderResponse cancelOrder(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
}
