package com.order.ecommerce.service;

import com.order.ecommerce.dto.OrderItemDto;
import com.order.ecommerce.dto.OrderResponseDto;
import com.order.ecommerce.dto.OrderDto;

public interface IOrderService {
    OrderResponseDto createOrder(OrderDto orderDto);

    OrderDto findOrderById(String id);

    void updateOrderStatus(String id, String status);

    void updateOrderQuantity(String orderId, OrderItemDto orderItemDto);
}
