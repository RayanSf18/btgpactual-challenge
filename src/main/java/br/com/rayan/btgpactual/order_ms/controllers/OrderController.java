package br.com.rayan.btgpactual.order_ms.controllers;

import br.com.rayan.btgpactual.order_ms.controllers.dto.ApiResponse;
import br.com.rayan.btgpactual.order_ms.controllers.dto.OrderResponse;
import br.com.rayan.btgpactual.order_ms.controllers.dto.PaginationResponse;
import br.com.rayan.btgpactual.order_ms.services.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(
            @PathVariable(value = "customerId") Long customerId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);
        return ResponseEntity.ok().body(new ApiResponse<>(Map.of("totalOnOrders", totalOnOrders),pageResponse.getContent(), PaginationResponse.fromPage(pageResponse)));
    }

}
