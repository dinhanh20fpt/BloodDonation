package com.swpproject.BloodDonation.dto.response;

import com.swpproject.BloodDonation.enums.BloodType;
import com.swpproject.BloodDonation.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
    private BloodType bloodType;
    private LocalDate birthday;
    private String sex;
    private String occupation;
}