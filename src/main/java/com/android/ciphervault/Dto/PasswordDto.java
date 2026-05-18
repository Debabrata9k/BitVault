package com.android.ciphervault.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    private Long id;
    private String appName;
    private String username;
    private String password;
    private String notes;
    private LocalDateTime createdAt;
}
