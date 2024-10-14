package com.microservice.product_service.service;

import com.microservice.product_service.dto.ProductRequest;
import com.microservice.product_service.model.Product;
import com.microservice.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .skuCode(productRequest.getSkuCode())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
    }

    @Override
    public boolean existsBySkuCode(String skuCode) {
        return productRepository.existsBySkuCode(skuCode);
    }
}
