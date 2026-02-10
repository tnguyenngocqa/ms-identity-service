package com.devteria.ms.identity.service.controller;

import com.devteria.ms.identity.service.dto.request.ApiResponse;
import com.devteria.ms.identity.service.dto.request.AuthRequest;
import com.devteria.ms.identity.service.dto.request.IntrospectRequest;
import com.devteria.ms.identity.service.dto.response.AuthResponse;
import com.devteria.ms.identity.service.dto.response.IntrospectResponse;
import com.devteria.ms.identity.service.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/token")
    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        var result = authService.authenticated(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
