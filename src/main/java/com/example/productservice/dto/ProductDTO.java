package com.example.productservice.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String category;
}
