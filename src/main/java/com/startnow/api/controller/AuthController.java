package com.startnow.api.controller;


import com.startnow.api.config.JwtService;
import com.startnow.api.dto.LoginRequestDto;
import com.startnow.api.dto.LoginResponseDto;
import com.startnow.api.dto.RegisterUserDto;
import com.startnow.api.model.User;
import com.startnow.api.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDto registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe.");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        final String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
