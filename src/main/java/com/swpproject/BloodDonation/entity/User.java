package com.swpproject.BloodDonation.entity;

import com.swpproject.BloodDonation.enums.BloodType;
import com.swpproject.BloodDonation.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name = "UserID")
    private String userId;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Address")
    private String address;

    @Column(name = "Password")
    private String password;

    @Column(name = "Phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type_Blood")
    private BloodType bloodType;

    @Column(name = "Birthday")
    private LocalDate birthday;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "Occupation")
    private String occupation;
}