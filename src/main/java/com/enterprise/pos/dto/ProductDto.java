package com.enterprise.pos.dto;

import com.enterprise.pos.model.Store;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long storeId;
    private String name;
    private String sku;
    private  String description;
    private Double MRP;
    private Double sellingPrice;
    private String brand;
    private String image;
    private Long catalogId;
//    private Catagory catogory;
    private Store store;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
