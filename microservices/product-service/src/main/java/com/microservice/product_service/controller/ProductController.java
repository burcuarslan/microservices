package com.microservice.product_service.controller;

import com.microservice.product_service.dto.ProductRequest;
import com.microservice.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping("/skuCode/{skuCode}/exists")
    @ResponseStatus(HttpStatus.OK)
    public boolean existsBySkuCode(@PathVariable String skuCode) {
        return productService.existsBySkuCode(skuCode);
    }
}
