package com.microservice.inventory_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.inventory_service.dto.InventoryAvailableResponse;
import com.microservice.inventory_service.model.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    InventoryAvailableResponse inventoryAvailableResponse = InventoryAvailableResponse.builder()
            .skuCode("samsung_s24_plus")
            .isAvailable(true)
            .build();

    InventoryAvailableResponse inventoryNoAvailableResponse = InventoryAvailableResponse.builder()
            .skuCode("samsung_s24_ultra")
            .isAvailable(false)
            .build();

    List<String> skuCodes = new ArrayList<>(Arrays.asList("samsung_s24_plus", "samsung_s24_ultra"));
    List<Inventory> inventoryList = new ArrayList<>(Arrays.asList(
            Inventory.builder()
                    .id(1L)
                    .skuCode("samsung_s24_plus")
                    .quantity(10)
                    .build(),

            Inventory.builder()
                    .id(5L)
                    .skuCode("samsung_s24_ultraaaaaaaaa")
                    .quantity(0)
                    .build()));

    @Test
    public void isInventoryAvailable() throws Exception {
//        Mockito.when(inventoryRepository.findBySkuCodeIn(skuCodes)).thenReturn(inventoryList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory")
                        .queryParam("skuCodes", skuCodes.toArray(new String[2]))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skuCode").value(inventoryAvailableResponse.getSkuCode()))
                .andExpect(jsonPath("$[0].isAvailable").value(inventoryAvailableResponse.getIsAvailable()))
                .andExpect(jsonPath("$[1].skuCode").value(inventoryNoAvailableResponse.getSkuCode()))
                .andExpect(jsonPath("$[1].isAvailable").value(inventoryNoAvailableResponse.getIsAvailable()));

//        verify(inventoryRepository,times(1)).findBySkuCodeIn(skuCodes);
    }

}