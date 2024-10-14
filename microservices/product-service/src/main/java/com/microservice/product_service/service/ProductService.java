package com.microservice.product_service.service;

import com.microservice.product_service.dto.ProductRequest;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    boolean existsBySkuCode(String skuCode);
}
