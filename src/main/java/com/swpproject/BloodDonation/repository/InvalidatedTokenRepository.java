package com.swpproject.BloodDonation.repository;

import com.swpproject.BloodDonation.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    Optional<InvalidatedToken> findById(String id);
}