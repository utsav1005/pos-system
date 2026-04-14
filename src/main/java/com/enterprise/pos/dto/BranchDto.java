package com.enterprise.pos.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BranchDto {

    private Long id;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private Long storeId;

    private List<String> workingDays;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private StoreDto store;

    private UserDto manager;



}
