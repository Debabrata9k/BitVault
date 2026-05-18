package com.android.ciphervault.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.android.ciphervault.Dto.LoginRequestDto;
import com.android.ciphervault.Dto.LoginResponseDto;
import com.android.ciphervault.Dto.ResetPasswordDto;
import com.android.ciphervault.Dto.SignupRequestDto;
import com.android.ciphervault.Dto.SignupResposeDto;
import com.android.ciphervault.Dto.VerifyOtpDto;
import com.android.ciphervault.Dto.Verifydto;
import com.android.ciphervault.Security.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResposeDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto)); 
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<Verifydto> verifyOtp(@RequestBody VerifyOtpDto dto) {
        return ResponseEntity.ok(authService.verifyOtp(dto));
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        return ResponseEntity.ok(authService.resendOtp(email));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(authService.forgotPassword(email));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }
}
