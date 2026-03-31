package com.enterprise.pos.controller;

import com.enterprise.pos.dto.ProductDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.service.ProductService;
import com.enterprise.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto ,
                                                    @RequestHeader("Authorization") String jwt) {
        UserDto user = userService.getUserFromJwtToken(jwt);
        ProductDto product = productService.createProduct(productDto, user);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductByStoreId(@PathVariable Long storeId ,
                                                       @RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(productService.getProductsByStoreId(storeId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDto productDto ,
                                                    @RequestHeader("Authorization") String jwt) {
        UserDto user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(productService.updateProduct(id, productDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        UserDto user = userService.getUserFromJwtToken(jwt);
        productService.deleteProduct(id, user);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/search/{storeId}/search")
    public ResponseEntity<List<ProductDto>> searchByKeyword(@PathVariable Long storeId ,
                                                      @RequestParam String keyword){
        return ResponseEntity.ok(productService.searchByKeyword(storeId , keyword));

    }

}
