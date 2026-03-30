package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.dto.ProductDto;
import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.exceptions.UserException;
import com.enterprise.pos.model.Product;
import com.enterprise.pos.model.Store;
import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.ProductRepository;
import com.enterprise.pos.repository.StoreRepository;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
        Store store = storeRepository.findById(productDto.getStore().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
                Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .brand(productDto.getBrand())
                .createdAt(productDto.getCreatedAt())
                .updatedAt(productDto.getUpdatedAt())
                .MRP(productDto.getMRP())
                .sku(productDto.getSku())
                .image(productDto.getImage())
                .sellingPrice(productDto.getSellingPrice())
                .id(productDto.getStore().getId())
                .build();

        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);

    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) {
        return null;
    }

    @Override
    public void deleteProduct(Long id, User user) {

    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        return List.of();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        return List.of();
    }
}
