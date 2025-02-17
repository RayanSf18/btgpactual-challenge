package br.com.rayan.btgpactual.order_ms.listener;

import br.com.rayan.btgpactual.order_ms.config.RabbitMqConfiguration;
import br.com.rayan.btgpactual.order_ms.listener.dto.OrderCreatedEvent;
import br.com.rayan.btgpactual.order_ms.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderService orderService;

    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMqConfiguration.ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        logger.info("Message consumed: {}", message);
        orderService.save(message.getPayload());
    }
}
