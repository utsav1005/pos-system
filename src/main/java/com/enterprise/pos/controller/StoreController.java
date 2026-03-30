package com.enterprise.pos.controller;

import com.enterprise.pos.dto.StoreDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.StoreStatus;
import com.enterprise.pos.service.StoreService;
import com.enterprise.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<StoreDto> createStore(
            @RequestBody StoreDto storeDto ,
            @RequestHeader("Authorization")String jwt) {
        UserDto user = userService.getUserFromJwtToken(jwt);
        modelMapper.map(user ,  User.class);
        return ResponseEntity.ok(storeService.createStore(storeDto,user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String jwt){
    return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    public ResponseEntity <StoreDto> getStoreByAdmin(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(storeService.getStoreByAdmin());
    }

    @GetMapping("/employee")
    public ResponseEntity <StoreDto> getStoreByEmployee(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id
                                            , @RequestBody StoreDto storeDto){
        return  ResponseEntity.ok(storeService.updateStore(id, storeDto));
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteStore(@PathVariable Long id){
            storeService.deleteStore(id);
            return ResponseEntity.ok("Successfully deleted!! ");
     }

     @PutMapping("/{id}/moderate")
     public ResponseEntity<StoreDto> updateStoreStatus(@PathVariable Long id, @RequestParam StoreStatus storeStatus){
         StoreDto storeDto = storeService.moderateStore(id, storeStatus);
         return ResponseEntity.ok(storeDto);
     }


}
