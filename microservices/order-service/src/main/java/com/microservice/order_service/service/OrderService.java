package com.microservice.order_service.service;

import com.microservice.order_service.dto.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
