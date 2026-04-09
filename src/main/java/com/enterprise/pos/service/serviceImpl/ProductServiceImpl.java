package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.dto.ProductDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.model.Category;
import com.enterprise.pos.model.Product;
import com.enterprise.pos.model.Store;
import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.CategoryRepository;
import com.enterprise.pos.repository.ProductRepository;
import com.enterprise.pos.repository.StoreRepository;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto, UserDto user) {
        Store store = storeRepository.findById(productDto.getStore().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));


        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .brand(productDto.getBrand())
                .category(category)
                .createdAt(productDto.getCreatedAt())
                .updatedAt(productDto.getUpdatedAt())
                .MRP(productDto.getMRP())
                .sku(productDto.getSku())
                .image(productDto.getImage())
                .store(store)
                .sellingPrice(productDto.getSellingPrice())
                .id(productDto.getStore().getId())
                .build();

        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);

    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, UserDto user){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));
        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setCategory(category);
        product.setCreatedAt(productDto.getCreatedAt());
        product.setUpdatedAt(LocalDateTime.now());
        product.setMRP(productDto.getMRP());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setStore(productDto.getStore());
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id, UserDto user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        List<Product> productByStoreId = productRepository.findByStoreId(store.getId());
        return productByStoreId.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
       return  productRepository.searchByKeyword(store.getId(), keyword).stream()
                .map(product -> modelMapper.map(product, ProductDto.class)).toList();
    }

}
