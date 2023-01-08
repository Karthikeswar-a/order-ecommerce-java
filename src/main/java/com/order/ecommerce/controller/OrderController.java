package com.order.ecommerce.controller;

import com.order.ecommerce.dto.OrderItemDto;
import com.order.ecommerce.dto.OrderResponseDto;
import com.order.ecommerce.dto.OrderDto;
import com.order.ecommerce.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final IOrderService orderService;

    /**
     * Creates order
     * @param orderDto
     * @return
     */
    @PostMapping
    @Operation(summary = "Create an order", description = "Create an order")
    public OrderResponseDto createOrder(@RequestBody OrderDto orderDto) {
        validateArgument(orderDto);
        return orderService.createOrder(orderDto);
    }

    /**
     * Finds Order by Id
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Find order", description = "Find order by id")
    public OrderDto findOrderBy(@PathVariable(name = "orderId") String orderId) {
        validateArgument(orderId == null || orderId.isEmpty(), "order id cannot be null or empty");
        return orderService.findOrderById(orderId);
    }

    /**
     * Updates order status
     * @param orderId
     * @param orderStatus
     */
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status", description = "Update order status")
    public void updateOrderStatus(@PathVariable("orderId") String orderId,
                                  @RequestParam(name = "orderStatus") String orderStatus) {
        validateArgument(orderId == null || orderId.isEmpty(), "order id cannot be null or empty");
        validateArgument(orderStatus == null || orderStatus.isEmpty(), "order status cannot be null or empty");
        orderService.updateOrderStatus(orderId, orderStatus);
    }

    @PatchMapping("/{orderId}/update")
    @Operation(summary = "Update order quantity", description = "Update order quantity")
    public void updateProductQuantity(@PathVariable("orderId") String orderId,
                                      @RequestBody OrderItemDto orderItemDto) {
        validateArgument(orderId == null || orderId.isEmpty(), "order id cannot be null or empty");
        validateArgument(orderItemDto == null, "Request can not be null");
        validateArgument(orderItemDto.getProductId() == null || orderItemDto.getProductId().isEmpty(), "product id cannot be null or empty");
        validateArgument(orderItemDto.getQuantity() == null || orderItemDto.getQuantity().isEmpty(), "product id cannot be null or empty");
        orderService.updateOrderQuantity(orderId, orderItemDto);
    }

    private void validateArgument(OrderDto orderDto) {
        validateArgument(orderDto.getCustomerId() == null || orderDto.getCustomerId().isEmpty(), "customer id cannot be null or empty");
        validateArgument(orderDto.getTitle() == null || orderDto.getTitle().isEmpty(), "title cannot be null or empty");
        validateArgument(orderDto.getPaymentMode() == null || orderDto.getPaymentMode().isEmpty(), "payment mode cannot be null or empty");
        validateArgument(orderDto.getBillingAddress() == null, "billing address cannot be null");
        validateArgument(orderDto.getOrderItems() == null || orderDto.getOrderItems().isEmpty(), "order items cannot be null or empty");
        validateArgument(orderDto.getOrderStatus() == null || orderDto.getOrderStatus().isEmpty(), "order status cannot be null or empty");
    }

    private void validateArgument(boolean condition, String message) {
        if (condition) {
            log.error("Error while processing request with message = {}", message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
