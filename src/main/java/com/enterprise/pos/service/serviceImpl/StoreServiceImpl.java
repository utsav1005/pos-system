package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.dto.StoreDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.exceptions.UnAuthorizedException;
import com.enterprise.pos.exceptions.UserException;
import com.enterprise.pos.model.Store;
import com.enterprise.pos.model.StoreContact;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.StoreStatus;
import com.enterprise.pos.repository.StoreRepository;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.security.jwt.UserPrincipal;
import com.enterprise.pos.service.StoreService;
import com.enterprise.pos.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelmapper;

    @Override
    public StoreDto createStore(StoreDto storeDto , UserDto user) {
        User storeAdmin = userRepository.findById(user.getId()).
                orElseThrow(() -> new UserException("User not found"));
        Store store = Store.builder()
                .id(storeDto.getId())
                .brand(storeDto.getBrand())
                .description(storeDto.getDescription())
                .contact(storeDto.getContact())
                .storeAdmin(storeAdmin)
                .createdAt(storeDto.getCreatedAt())
                .updatedAt(storeDto.getUpdatedAt())
                .storeType(storeDto.getStoreType())
                .status(StoreStatus.PENDING)
                .build();

        Store saveStore = storeRepository.save(store);
        StoreDto response = modelmapper.map(saveStore, StoreDto.class);
        response.setStoreAdmin(modelmapper.map(storeAdmin,UserDto.class));
        return response;
    }

    @Override
    public StoreDto getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return modelmapper.map(store , StoreDto.class);
    }

    @Override
    public List<StoreDto> getAllStores() {
        List<Store> allStore = storeRepository.findAll();
        return allStore.stream()
                .map(store -> modelmapper.map(store, StoreDto.class))
                .toList();
    }

    @Override
    @JsonIgnore
    public StoreDto getStoreByAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication object is {}", authentication);
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedException("User not authenticated");
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        Store store = storeRepository.findByStoreAdminId(userId);
        if(store == null) {
            throw new ResourceNotFoundException("Store not found");
        }
        return modelmapper.map(store,StoreDto.class);
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) {
        UserDto currentUser = userService.getCurrentUser();
        if(currentUser == null){
            throw new UserException("User has no permission to update store ");
        }
        Store existing = storeRepository.findByStoreAdminId(currentUser.getId());
        if (existing == null){
            throw new  ResourceNotFoundException("Store not found");
        }
        existing.setBrand(storeDto.getBrand());
        existing.setDescription(storeDto.getDescription());

        if(storeDto.getStoreType() != null){
            existing.setStoreType(storeDto.getStoreType());
        }
        if(storeDto.getContact() != null){
            StoreContact contact = StoreContact.builder()
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .address(storeDto.getContact().getAddress())
                    .build();
            existing.setContact(contact);
        }
        Store save = storeRepository.save(existing);
        return modelmapper.map(save, StoreDto.class);
    }

    @Override
    public void deleteStore(Long id) {
        StoreDto storeDto = getStoreByAdmin();
        Store store = modelmapper.map(storeDto, Store.class);
        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() {
        UserDto user = userService.getCurrentUser();
        if(user == null){
            throw new UserException("You don't have Permission to access the Store");
        }
        Store store = user.getStore();
        return modelmapper.map(store, StoreDto.class);
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus storeStatus) {
        Store store = storeRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        store.setStatus(storeStatus);
        Store saveStore = storeRepository.save(store);
        return modelmapper.map(saveStore, StoreDto.class);
    }
}
