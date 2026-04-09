package com.enterprise.pos.service;

import com.enterprise.pos.dto.ProductDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.model.User;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto , UserDto user);
    ProductDto updateProduct(Long id, ProductDto productDto , UserDto user);
    void deleteProduct(Long id, UserDto user);
    List<ProductDto> getProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword (Long storeId , String keyword);


}
