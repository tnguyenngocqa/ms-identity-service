package com.devteria.ms.identity.service.controller;

import com.devteria.ms.identity.service.dto.request.ApiResponse;
import com.devteria.ms.identity.service.dto.request.AuthRequest;
import com.devteria.ms.identity.service.dto.response.AuthResponse;
import com.devteria.ms.identity.service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        return ApiResponse.<AuthResponse>builder()
                .result(AuthResponse.builder()
                        .authenticated(authService.authenticated(request))
                        .build())
                .build();
    }
}
