package com.swpproject.BloodDonation.service;

import com.swpproject.BloodDonation.entity.User;
import com.swpproject.BloodDonation.enums.Role;
import com.swpproject.BloodDonation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = User.builder()
                    .userId("admin001")
                    .fullName("Admin User")
                    .email("admin@blooddonation.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phoneNumber("1234567890")
                    .address("Admin Address")
                    .role(Role.ADMIN)
                    .bloodType(com.swpproject.BloodDonation.enums.BloodType.O_POSITIVE)
                    .birthday(LocalDate.now())
                    .sex("M")
                    .occupation("Admin")
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        }
    }
}