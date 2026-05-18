package com.android.ciphervault.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.android.ciphervault.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
