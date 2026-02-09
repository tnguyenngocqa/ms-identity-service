package com.devteria.ms.identity.service.controller;

import com.devteria.ms.identity.service.dto.request.ApiResponse;
import com.devteria.ms.identity.service.dto.request.UserCreationRequest;
import com.devteria.ms.identity.service.dto.request.UserUpdateRequest;
import com.devteria.ms.identity.service.dto.response.UserResponse;
import com.devteria.ms.identity.service.entity.User;
import com.devteria.ms.identity.service.mapper.UserMapper;
import com.devteria.ms.identity.service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }
}
