package com.microservice.order_service.service;

import com.microservice.order_service.dto.InventoryAvailableResponse;
import com.microservice.order_service.dto.OrderItemRequest;
import com.microservice.order_service.dto.OrderRequest;
import com.microservice.order_service.event.OrderPlacedEvent;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.model.OrderItem;
import com.microservice.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(this::map).toList();
        List<String> skuCodes = orderItems.stream().map(OrderItem::getSkuCode).toList();
        boolean allProductsAvailable = getInventoryAvailableResponses(skuCodes);

        if (allProductsAvailable) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .orderItems(orderItems)
                    .build();
            kafkaTemplate.send(kafkaTopic, new OrderPlacedEvent(order.getOrderNumber()));
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("All products are not available");
        }

    }

    private boolean getInventoryAvailableResponses(List<String> skuCodes) {

        InventoryAvailableResponse[] inventoryAvailableResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryAvailableResponse[].class)
                .block();
        return Arrays.stream(inventoryAvailableResponses).allMatch(InventoryAvailableResponse::getIsAvailable);
    }

    private OrderItem map(OrderItemRequest orderItemRequest) {
        return OrderItem.builder()
                .skuCode(orderItemRequest.getSkuCode())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .build();
    }
}
