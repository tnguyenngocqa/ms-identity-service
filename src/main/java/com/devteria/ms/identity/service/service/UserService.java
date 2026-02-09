package com.devteria.ms.identity.service.service;

import com.devteria.ms.identity.service.dto.request.UserCreationRequest;
import com.devteria.ms.identity.service.dto.request.UserUpdateRequest;
import com.devteria.ms.identity.service.dto.response.UserResponse;
import com.devteria.ms.identity.service.entity.User;
import com.devteria.ms.identity.service.exception.AppException;
import com.devteria.ms.identity.service.exception.ErrorCode;
import com.devteria.ms.identity.service.mapper.UserMapper;
import com.devteria.ms.identity.service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        // check if user is existing
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // use mapstruct = map fields the same name
        User user = userMapper.toUser(request);

        // save database
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // use mapstruct = map fields the same name
        userMapper.updateUser(user, request);

        // save database
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
