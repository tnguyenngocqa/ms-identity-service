package com.devteria.ms.identity.service.service;

import com.devteria.ms.identity.service.dto.request.AuthRequest;
import com.devteria.ms.identity.service.exception.AppException;
import com.devteria.ms.identity.service.exception.ErrorCode;
import com.devteria.ms.identity.service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;

    public boolean authenticated(AuthRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // check password input and password saved in database
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
