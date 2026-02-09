package com.devteria.ms.identity.service.mapper;

import com.devteria.ms.identity.service.dto.request.UserCreationRequest;
import com.devteria.ms.identity.service.dto.request.UserUpdateRequest;
import com.devteria.ms.identity.service.dto.response.UserResponse;
import com.devteria.ms.identity.service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
