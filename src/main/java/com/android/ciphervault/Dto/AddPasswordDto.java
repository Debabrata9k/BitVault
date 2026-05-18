package com.android.ciphervault.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPasswordDto {
    private String appName;
    private String username;
    private String password;
    private String notes;
}
