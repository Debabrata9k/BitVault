package com.android.ciphervault.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.android.ciphervault.Entity.Password;
import com.android.ciphervault.Entity.User;

public interface PasswordRepository extends JpaRepository<Password, Long>{
    List<Password> findByUser(User user);
    List<Password> findByUserAndAppNameContainingIgnoreCase(User user, String keyword);
    Optional<Password> findByIdAndUser(Long id, User user);
}
