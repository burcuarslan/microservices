package com.microservice.product_service.repository;

import com.microservice.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsBySkuCode(String skuCode);
}
