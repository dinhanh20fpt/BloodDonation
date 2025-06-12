package com.swpproject.BloodDonation.mapper;

import com.swpproject.BloodDonation.dto.request.UserCreationRequest;
import com.swpproject.BloodDonation.dto.request.UserUpdateRequest;
import com.swpproject.BloodDonation.dto.response.UserResponse;
import com.swpproject.BloodDonation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId", expression = "java(userCreationRequest.getUserId() != null ? userCreationRequest.getUserId() : generateUserId())")
    @Mapping(target = "password", ignore = true) // Will be set separately with encoded password
    @Mapping(target = "role", ignore = true) // Will be set to Role.USER in service
    User toEntity(UserCreationRequest userCreationRequest);

    UserResponse toResponse(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", ignore = true) // Email should not be updated
    @Mapping(target = "role", ignore = true) // Role should not be updated
    void updateEntity(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    default String generateUserId() {
        return "user_" + System.currentTimeMillis(); // Placeholder, customize as needed
    }
}