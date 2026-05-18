package com.android.ciphervault.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.android.ciphervault.Dto.AddPasswordDto;
import com.android.ciphervault.Dto.PasswordDto;
import com.android.ciphervault.Entity.Password;
import com.android.ciphervault.Entity.User;
import com.android.ciphervault.Repository.PasswordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordRepository passwodRepository;
    private final ModelMapper modelMapper;
    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
    public List<PasswordDto> getPasswords() {
        User user = getCurrentUser();
        List<Password> passwords = passwodRepository.findByUser(user);
        return passwords.stream()
                .map(password -> modelMapper.map(password, PasswordDto.class))
                .toList();
    }
    public List<PasswordDto> searchPasswordsByAppName(String appName) {
        User user = getCurrentUser();
        return passwodRepository
                .findByUserAndAppNameContainingIgnoreCase(user, appName)
                .stream()
                .map(password -> modelMapper.map(password, PasswordDto.class))
                .toList();
    }
    public PasswordDto createNewPassword(AddPasswordDto addPasswordDto) {
        User user = getCurrentUser();
        Password password = modelMapper.map(addPasswordDto, Password.class);
        password.setUser(user);
        Password savedPassword = passwodRepository.save(password);
        return modelMapper.map(savedPassword, PasswordDto.class);
    }
    public PasswordDto updatePassword(Long id, AddPasswordDto addPasswordDto) {
        User user = getCurrentUser();
        Password password = passwodRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Password not found"));
        password.setAppName(addPasswordDto.getAppName());
        password.setUsername(addPasswordDto.getUsername());
        password.setPassword(addPasswordDto.getPassword());
        password.setNotes(addPasswordDto.getNotes());
        Password updatedPassword = passwodRepository.save(password);
        return modelMapper.map(updatedPassword, PasswordDto.class);
    }
    public void deletePassword(Long id) {
        User user = getCurrentUser();
        Password password = passwodRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Password not found"));
        passwodRepository.delete(password);
    }
}
