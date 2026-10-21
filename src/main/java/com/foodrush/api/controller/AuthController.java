package com.foodrush.api.controller;

import com.foodrush.api.dto.request.LoginRequestDTO;
import com.foodrush.api.dto.request.RefreshTokenRequestDTO;
import com.foodrush.api.dto.request.UserRequestDTO;
import com.foodrush.api.dto.response.AuthResponseDTO;
import com.foodrush.api.dto.response.UserResponseDTO;
import com.foodrush.api.enums.RoleType;
import com.foodrush.api.exception.BusinessException;
import com.foodrush.api.model.User;
import com.foodrush.api.repository.UserRepository;
import com.foodrush.api.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado no sistema.");
        }
        if (userRepository.existsByPhone(dto.phone())) {
            throw new BusinessException("Telefone já cadastrado no sistema.");
        }
        if (userRepository.existsByCpf(dto.cpf())) {
            throw new BusinessException("CPF já cadastrado no sistema.");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .phone(dto.phone())
                .cpf(dto.cpf())
                .role(RoleType.valueOf(dto.role().toUpperCase()))
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromEntity(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO requestDTO) {
        if (!tokenProvider.validateToken(requestDTO.refreshToken())) {
            throw new BusinessException("Refresh token inválido ou expirado.");
        }

        String username = tokenProvider.getUsernameFromJWT(requestDTO.refreshToken());
        String newAccessToken = tokenProvider.generateAccessTokenFromUsername(username);

        // Retorna um novo Access Token mantendo o mesmo Refresh Token (ou gerando outro, se preferir rota rotativa)
        return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, requestDTO.refreshToken()));
    }
}