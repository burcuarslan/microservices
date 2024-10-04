package com.microservice.inventory_service;

import com.microservice.inventory_service.model.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = Inventory.builder()
                    .skuCode("samsung_s24_plus")
                    .quantity(10)
                    .build();

            Inventory inventory2 = Inventory.builder()
                    .skuCode("samsung_s24_ultra")
                    .quantity(0)
                    .build();

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory2);
        };
    }
}
