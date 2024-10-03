package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryAvailableResponse;
import com.microservice.inventory_service.model.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<InventoryAvailableResponse> isInventoryAvailable(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);

        Map<String, Boolean> skuAvailabilityMap = inventories.stream()
                .collect(Collectors.toMap(
                        Inventory::getSkuCode,
                        inventory -> inventory.getQuantity() > 0
                ));

        return skuCodes.stream()
                .map(skuCode -> createAvailabilityResponse(skuCode, skuAvailabilityMap))
                .collect(Collectors.toList());
    }

    private InventoryAvailableResponse createAvailabilityResponse(String skuCode, Map<String, Boolean> skuAvailabilityMap) {
        boolean isAvailable = skuAvailabilityMap.getOrDefault(skuCode, false);
        return InventoryAvailableResponse.builder()
                .skuCode(skuCode)
                .isAvailable(isAvailable)
                .build();
    }

}
