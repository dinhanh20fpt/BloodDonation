package com.swpproject.BloodDonation.service;

import com.swpproject.BloodDonation.dto.request.UserCreationRequest;
import com.swpproject.BloodDonation.dto.request.UserUpdateRequest;
import com.swpproject.BloodDonation.dto.response.UserResponse;
import com.swpproject.BloodDonation.entity.User;
import com.swpproject.BloodDonation.enums.Role;
import com.swpproject.BloodDonation.exception.AppException;
import com.swpproject.BloodDonation.exception.ErrorCode;
import com.swpproject.BloodDonation.mapper.UserMapper;
import com.swpproject.BloodDonation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InvalidatedTokenService invalidatedTokenService;

    public UserResponse registerUser(UserCreationRequest userCreationRequest) {
        if (userRepository.findByEmail(userCreationRequest.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = UserMapper.INSTANCE.toEntity(userCreationRequest);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setRole(Role.USER);
        return UserMapper.INSTANCE.toResponse(userRepository.save(user));
    }

    public User findByEmailForAuthentication(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return UserMapper.INSTANCE.toResponse(user);
    }

    public User findEntityByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(String userId, UserUpdateRequest updateRequest, String currentUserEmail) {
        User existingUser = findEntityByUserId(userId);
        if (!existingUser.getEmail().equals(currentUserEmail) && !hasAdminRole(currentUserEmail)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        UserMapper.INSTANCE.updateEntity(existingUser, updateRequest);
        if (updateRequest.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        return UserMapper.INSTANCE.toResponse(userRepository.save(existingUser));
    }

    public void deleteUser(String userId, String currentUserEmail) {
        User user = findEntityByUserId(userId);
        if (!user.getEmail().equals(currentUserEmail) && !hasAdminRole(currentUserEmail)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        userRepository.delete(user);
    }

    public void invalidateToken(String token) {
        invalidatedTokenService.invalidateToken(token);
    }

    /**
     * Checks if a user with the given email has the ADMIN role.
     * @param email The email of the user to check.
     * @return true if the user is an admin, false otherwise.
     * @throws AppException if the user is not found.
     */
    public boolean hasAdminRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user.getRole() == Role.ADMIN;
    }
}