package com.enterprise.pos.repository;

import com.enterprise.pos.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreAdminId(Long storeAdminId);
}
