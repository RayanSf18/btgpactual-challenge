package br.com.rayan.btgpactual.order_ms.services;

import br.com.rayan.btgpactual.order_ms.controllers.dto.OrderResponse;
import br.com.rayan.btgpactual.order_ms.entities.Order;
import br.com.rayan.btgpactual.order_ms.entities.OrderItem;
import br.com.rayan.btgpactual.order_ms.listener.dto.OrderCreatedEvent;
import br.com.rayan.btgpactual.order_ms.repositories.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {
        var newOrder = new Order();
        newOrder.setOrderId(event.codigoPedido());
        newOrder.setCustomerId(event.codigoCliente());
        newOrder.setItems(getOrderItems(event));
        newOrder.setTotal(getTotal(event));

        orderRepository.save(newOrder);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromOrder);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens().stream()
                        .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco())).toList();
    }
}
