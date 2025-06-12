package com.swpproject.BloodDonation.dto.request;

import com.swpproject.BloodDonation.enums.BloodType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreationRequest {
    private String userId; // Optional, can be generated
    @NotBlank
    private String fullName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String phoneNumber;
    private String address;
    private BloodType bloodType;
    private LocalDate birthday;
    private String sex;
    private String occupation;
}