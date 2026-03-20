package com.enterprise.pos.repository;

import com.enterprise.pos.model.User;
import com.enterprise.pos.security.jwt.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
