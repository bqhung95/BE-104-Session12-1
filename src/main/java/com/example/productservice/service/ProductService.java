package com.example.productservice.service;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * CACHE-ASIDE:
     *  - Lần 1: chưa có trong Redis → query DB (SQL) → lưu cache.
     *  - Lần 2..n: có trong Redis → trả về ngay (<10ms).
     */
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Long id) {
        log.info(">>> [DB HIT] Querying product id = {}", id);
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return toDTO(p);
    }

    /**
     * UPDATE: xóa cache ngay lập tức → lần đọc sau tự nạp lại từ DB.
     */
    @CacheEvict(value = "products", key = "#product.id")
    public ProductDTO updateProduct(ProductDTO product) {
        log.info(">>> [UPDATE] Product id = {} -> evict cache", product.getId());
        Product entity = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + product.getId()));

        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setImageUrl(product.getImageUrl());
        entity.setPrice(product.getPrice());
        entity.setCategory(product.getCategory());
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(productRepository.save(entity));
    }

    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .imageUrl(p.getImageUrl())
                .price(p.getPrice())
                .category(p.getCategory())
                .build();
    }
}
