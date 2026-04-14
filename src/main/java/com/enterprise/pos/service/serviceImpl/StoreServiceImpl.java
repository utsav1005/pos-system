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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Transactional
    public StoreDto createStore(StoreDto storeDto , UserDto user) {
        User storeAdmin = userRepository.findById(user.getId()).
                orElseThrow(() -> new UserException("User not found"));

        Store store = Store.builder()
                .brand(storeDto.getBrand())
                .description(storeDto.getDescription())
                .contact(storeDto.getContact())
                .createdAt(storeDto.getCreatedAt())
                .updatedAt(storeDto.getUpdatedAt())
                .storeAdmin(storeAdmin)
                .storeType(storeDto.getStoreType())
                .status(StoreStatus.PENDING)
                .build();

        Store saveStore = storeRepository.save(store);

        return StoreDto.builder()
                .id(saveStore.getId())
                .brand(saveStore.getBrand())
                .storeAdmin(user)
                .storeType(saveStore.getStoreType())
                .description(saveStore.getDescription())
                .status(saveStore.getStatus())
                .contact(saveStore.getContact())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public StoreDto getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        User storeAdmin = store.getStoreAdmin();
        UserDto userDto = modelmapper.map(storeAdmin, UserDto.class);

        return StoreDto.builder()
                .id(store.getId())
                .storeAdmin(userDto)
               .contact(store.getContact())
               .brand(store.getBrand())
               .status(store.getStatus())
               .description(store.getDescription())
               .updatedAt(store.getUpdatedAt())
               .createdAt(store.getCreatedAt())
               .storeType(store.getStoreType())
               .build();
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
