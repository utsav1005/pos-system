package com.enterprise.pos.service;

import com.enterprise.pos.dto.ProductDto;
import com.enterprise.pos.model.User;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto , User user);
    ProductDto updateProduct(Long id, ProductDto productDto , User user);
    void deleteProduct(Long id, User user);
    List<ProductDto> getProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword (Long storeId , String keyword);


}
