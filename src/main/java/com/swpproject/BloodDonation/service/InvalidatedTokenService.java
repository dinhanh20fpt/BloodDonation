package com.swpproject.BloodDonation.service;

import com.swpproject.BloodDonation.entity.InvalidatedToken;
import com.swpproject.BloodDonation.repository.InvalidatedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InvalidatedTokenService {

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    public void invalidateToken(String token) {
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(token);
        invalidatedToken.setExpiryTime(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24 hours
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokenRepository.findById(token).isPresent();
    }
}