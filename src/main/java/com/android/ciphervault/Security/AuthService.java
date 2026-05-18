package com.android.ciphervault.Security;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.android.ciphervault.Dto.LoginRequestDto;
import com.android.ciphervault.Dto.LoginResponseDto;
import com.android.ciphervault.Dto.ResetPasswordDto;
import com.android.ciphervault.Dto.SignupRequestDto;
import com.android.ciphervault.Dto.SignupResposeDto;
import com.android.ciphervault.Dto.VerifyOtpDto;
import com.android.ciphervault.Dto.Verifydto;
import com.android.ciphervault.Entity.User;
import com.android.ciphervault.Repository.UserRepository;
import com.android.ciphervault.Service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private String generateOtp() {
        return String.valueOf(
                new java.security.SecureRandom().nextInt(900000) + 100000
        );
    }
    public SignupResposeDto signup(SignupRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        String otp = generateOtp();
        if (user != null) {
            if (user.isVerified()) {
                throw new RuntimeException("User already exists");
            }
            user.setOtp(passwordEncoder.encode(otp));
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            user.setName(dto.getName());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            user = new User();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setVerified(false);
            user.setOtp(passwordEncoder.encode(otp));
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        }
        User savedUser = userRepository.save(user);
        emailService.sendOtp(savedUser.getEmail(), otp);
        return modelMapper.map(savedUser, SignupResposeDto.class);
    }
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + loginRequestDto.getEmail()));
        if (!user.isVerified()) {
            throw new IllegalArgumentException("Please verify your email with OTP first");
        }
        if (!passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDto(token, user.getName(), user.getEmail());
    }
    public Verifydto verifyOtp(VerifyOtpDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new RuntimeException("OTP not available");
        }
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        if (!passwordEncoder.matches(dto.getOtp(), user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return new Verifydto("Email verified successfully");
    }
    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // if (user.isVerified()) {
        //     throw new RuntimeException("Email already verified");
        // }
        String otp = generateOtp();
        user.setOtp(passwordEncoder.encode(otp));
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        emailService.sendOtp(user.getEmail(), otp);
        return "OTP resent successfully";
    }
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String otp = generateOtp();
        user.setOtp(passwordEncoder.encode(otp));
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        emailService.sendOtp(email, otp);
        return "Password reset OTP sent";
    }
    public String resetPassword(ResetPasswordDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new RuntimeException("OTP not available");
        }
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
        if (!passwordEncoder.matches(dto.getOtp(), user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return "Password reset successful";
    }
}
