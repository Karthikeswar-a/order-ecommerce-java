package com.order.ecommerce.validator;

import com.order.ecommerce.entity.Order;
import com.order.ecommerce.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Validator {

    private static final List<String> restrictedStatus = Arrays.asList(OrderStatus.CANCELLED.name(), OrderStatus.COMPLETED.name(), OrderStatus.SHIPPED.name());

    private static final Predicate<String> orderStatusValidator = restrictedStatus::contains;


    private static void orderStatusValidator(String orderStatus) {
        validateArgument(orderStatusValidator.test(orderStatus), "Order is restricted for update as Status : " + orderStatus);
    }

    public static void productValidator(Order order, String productId) throws Exception {

        orderStatusValidator(order.getOrderStatus());
        validateArgument(order.getOrderItems().stream()
                        .noneMatch(orderItem ->
                                orderItem.getProduct().getProductId().equalsIgnoreCase(productId)),
                "No product is available in order with productId : " + productId);
    }

    public static void validateArgument(boolean condition, String message) {
        if (condition) {
            log.error("Error while processing request with message = {}", message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
