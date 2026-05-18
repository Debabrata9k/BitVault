package com.android.ciphervault.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.android.ciphervault.Dto.AddPasswordDto;
import com.android.ciphervault.Dto.PasswordDto;
import com.android.ciphervault.Service.PasswordService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/passwords")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;
    @GetMapping
    public ResponseEntity<List<PasswordDto>> getAllPasswords() {
        return ResponseEntity.ok(passwordService.getPasswords());
    }
    @GetMapping("/search")
    public ResponseEntity<List<PasswordDto>> searchPasswords(@RequestParam String appName) {
        return ResponseEntity.ok(passwordService.searchPasswordsByAppName(appName));
    }
    @PostMapping
    public ResponseEntity<PasswordDto> createNewPassword(@RequestBody AddPasswordDto addPasswordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordService.createNewPassword(addPasswordDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PasswordDto> updatePassword(@PathVariable Long id, @RequestBody AddPasswordDto addPasswordDto) {
        return ResponseEntity.ok(passwordService.updatePassword(id, addPasswordDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<PasswordDto> deletePassword(@PathVariable Long id) {
        passwordService.deletePassword(id);
        return ResponseEntity.noContent().build();
    }
}
