package br.com.rayan.btgpactual.order_ms.controllers.dto;

import br.com.rayan.btgpactual.order_ms.entities.Order;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        Long customerId,
        BigDecimal total
) {
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(order.getOrderId(), order.getCustomerId(), order.getTotal());
    }
}
