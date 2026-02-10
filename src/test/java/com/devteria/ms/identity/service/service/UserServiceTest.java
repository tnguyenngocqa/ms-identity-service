package com.devteria.ms.identity.service.service;

import com.devteria.ms.identity.service.dto.request.UserCreationRequest;
import com.devteria.ms.identity.service.dto.response.UserResponse;
import com.devteria.ms.identity.service.entity.User;
import com.devteria.ms.identity.service.exception.AppException;
import com.devteria.ms.identity.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("795c3f5b-f3cc-4c58-91cf-557c3fca27ce")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        user = User.builder()
                .id("795c3f5b-f3cc-4c58-91cf-557c3fca27ce")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();
    }

    @Test
    void test_CreateUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        assertThat(response.getId()).isEqualTo("795c3f5b-f3cc-4c58-91cf-557c3fca27ce");
        assertThat(response.getUsername()).isEqualTo("john");
    }

    @Test
    void test_CreateUser_userExisted_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
        assertThat(exception.getErrorCode().getMessage()).isEqualTo("User existed");
    }
}
