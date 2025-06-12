package com.swpproject.BloodDonation.controller;

import com.swpproject.BloodDonation.dto.request.UserCreationRequest;
import com.swpproject.BloodDonation.dto.request.UserUpdateRequest;
import com.swpproject.BloodDonation.dto.response.UserResponse;
import com.swpproject.BloodDonation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserCreationRequest userCreationRequest) {
        return ResponseEntity.ok(userService.registerUser(userCreationRequest));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.findByEmail(userDetails.getUsername()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        if (!userService.hasAdminRole(userDetails.getUsername())) {
            throw new RuntimeException("Unauthorized");
        }
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId, @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.findByEmail(userService.findEntityByUserId(userId).getEmail());
        if (user == null || (!userDetails.getUsername().equals(user.getEmail()) && !userService.hasAdminRole(userDetails.getUsername()))) {
            throw new RuntimeException("Unauthorized or User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest updateRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.updateUser(userId, updateRequest, userDetails.getUsername()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Logout is handled by Spring Security's formLogout configuration
        return ResponseEntity.ok().build();
    }
}