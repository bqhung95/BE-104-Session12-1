package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        ProductDTO dto = productService.getProductById(id);
        long elapsed = System.currentTimeMillis() - start;
        return ResponseEntity.ok()
                .header("X-Response-Time-ms", String.valueOf(elapsed))
                .header("X-Cache", elapsed < 50 ? "HIT" : "MISS")
                .body(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        dto.setId(id);
        return productService.updateProduct(dto);
    }
}
