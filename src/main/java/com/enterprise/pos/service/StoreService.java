package com.enterprise.pos.service;

import com.enterprise.pos.dto.StoreDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.StoreStatus;

import java.util.List;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto , UserDto user);
    StoreDto getStoreById(Long id);
    List<StoreDto> getAllStores();
    StoreDto getStoreByAdmin();
    StoreDto updateStore(Long id , StoreDto storeDto);
    void deleteStore(Long id);
    StoreDto getStoreByEmployee();
    //Changing Store Status
    StoreDto moderateStore(Long id , StoreStatus storeStatus);


}
