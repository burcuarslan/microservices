package com.microservice.inventory_service.controller;

import com.microservice.inventory_service.dto.CreateInventoryRequest;
import com.microservice.inventory_service.dto.CreateInventoryResponse;
import com.microservice.inventory_service.dto.InventoryAvailableResponse;
import com.microservice.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryAvailableResponse> isInventoryAvailable(@RequestParam List<String> skuCodes) {
        return inventoryService.isInventoryAvailable(skuCodes);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CreateInventoryResponse addInventory(@RequestBody CreateInventoryRequest createInventoryRequest) {
        return inventoryService.updateOrCreateInventory(createInventoryRequest);
    }
}
