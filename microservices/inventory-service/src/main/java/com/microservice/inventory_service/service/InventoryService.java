package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.CreateInventoryRequest;
import com.microservice.inventory_service.dto.CreateInventoryResponse;
import com.microservice.inventory_service.dto.InventoryAvailableResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryAvailableResponse> isInventoryAvailable(List<String> skuCodes);
    CreateInventoryResponse updateOrCreateInventory(CreateInventoryRequest createInventoryRequest);
}
