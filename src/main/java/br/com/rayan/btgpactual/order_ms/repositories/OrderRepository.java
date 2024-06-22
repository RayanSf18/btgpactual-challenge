package br.com.rayan.btgpactual.order_ms.repositories;

import br.com.rayan.btgpactual.order_ms.controllers.dto.OrderResponse;
import br.com.rayan.btgpactual.order_ms.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, Long> {
    Page<Order> findAllByCustomerId(Long customerId, PageRequest pageRequest);
}
