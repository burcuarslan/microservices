package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.CreateInventoryRequest;
import com.microservice.inventory_service.dto.CreateInventoryResponse;
import com.microservice.inventory_service.dto.InventoryAvailableResponse;
import com.microservice.inventory_service.model.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WebClient.Builder webClientBuilder;

    @Transactional(readOnly = true)
    @Override
    public List<InventoryAvailableResponse> isInventoryAvailable(List<String> skuCodes) {
//        log.info("wait started");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("wait ended");
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

    @Override
    public CreateInventoryResponse updateOrCreateInventory(CreateInventoryRequest createInventoryRequest) {
        String skuCode = createInventoryRequest.getSkuCode();
        int quantity = createInventoryRequest.getQuantity();
        Optional<Inventory> inventoryOptional = Optional.ofNullable(inventoryRepository.findBySkuCode(skuCode));

        if (inventoryOptional.isPresent()) {
            return updateInventoryQuantity(inventoryOptional.get(),quantity);
        }

        return createInventoryIfProductExists(skuCode, quantity)
                .map(inventory -> buildResponse(skuCode, "Stok bilgisi oluşturuldu", true))
                .orElseGet(() -> buildResponse(skuCode, "SKU koduna ait ürün bulunamadı!", false));
    }

    private Optional<Inventory> createInventoryIfProductExists(String skuCode, int quantity) {
        if (productExistsBySkuCode(skuCode)) {
            Inventory newInventory = createInventory(skuCode, quantity);
            return Optional.of(newInventory);
        }
        return Optional.empty();
    }

    private CreateInventoryResponse updateInventoryQuantity(Inventory inventory, int quantity) {
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
        return buildResponse(inventory.getSkuCode(), "Stok bilgisi güncellendi", true);
    }

    private CreateInventoryResponse buildResponse(String skuCode, String message, boolean success) {
        return CreateInventoryResponse.builder()
                .skuCode(skuCode)
                .message(message)
                .success(success)
                .build();
    }

    private Inventory createInventory(String skuCode, Integer quantity) {
        Inventory newInventory = Inventory.builder()
                .skuCode(skuCode)
                .quantity(quantity)
                .build();
        return inventoryRepository.save(newInventory);
    }

    private boolean productExistsBySkuCode(String skuCode) {

        return Boolean.TRUE.equals(webClientBuilder.build().get()
                .uri("http://product-service/api/product/skuCode/{skuCode}/exists", skuCode)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }

    private InventoryAvailableResponse createAvailabilityResponse(String skuCode, Map<String, Boolean> skuAvailabilityMap) {
        boolean isAvailable = skuAvailabilityMap.getOrDefault(skuCode, false);
        return InventoryAvailableResponse.builder()
                .skuCode(skuCode)
                .isAvailable(isAvailable)
                .build();
    }

}
