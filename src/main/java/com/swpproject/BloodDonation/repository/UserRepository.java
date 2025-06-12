package com.swpproject.BloodDonation.repository;

import com.swpproject.BloodDonation.entity.User;
import com.swpproject.BloodDonation.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    List<User> findByRole(Role role);
}