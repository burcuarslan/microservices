package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryAvailableResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryAvailableResponse> isInventoryAvailable(List<String> skuCodes);
}
