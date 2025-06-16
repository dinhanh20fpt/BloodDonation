package com.swpproject.BloodDonation.service;

import com.nimbusds.jwt.SignedJWT;
import com.swpproject.BloodDonation.dto.request.LoginRequest;
import com.swpproject.BloodDonation.dto.request.RefreshTokenRequest;
import com.swpproject.BloodDonation.dto.response.LoginResponse;
import com.swpproject.BloodDonation.dto.response.RefreshTokenResponse;
import com.swpproject.BloodDonation.entity.InvalidtedToken;
import com.swpproject.BloodDonation.entity.User;
import com.swpproject.BloodDonation.repository.InvalidtedTokenRepository;
import com.swpproject.BloodDonation.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final InvalidtedTokenRepository invalidtedTokenRepository;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }catch (BadCredentialsException e) {
            throw e;
        }
    }

    public void logout(String accessToken) throws ParseException {
        // 1. Kiểm tra xem token đó có phải là token của hệ thống mình sản xuất ra hay không
        SignedJWT signedJWT = SignedJWT.parse(accessToken);

        // 2. Đánh dấu token đó hết hiệu lực, và không có quyền truy cập vào hệ thống nữa, dù cho thời gian token còn hiệu lực
        InvalidtedToken invalidtedToken = InvalidtedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .token(accessToken)
                .expirationTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();
        // 3. Lưu token vào data, từ lần sau kiểm tra token người dùng gửi có trong database hay không
        invalidtedTokenRepository.save(invalidtedToken);
        log.info("Logout successfully");
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException {
        if(StringUtils.isBlank(request.getRefreshToken()))
            throw new RuntimeException("Token cannot be blank");

        SignedJWT signedJWT = SignedJWT.parse(request.getRefreshToken());

        if(signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date()))
            throw new RuntimeException("Token expired time");

        Optional<InvalidtedToken> invalidtedToken = invalidtedTokenRepository.findById(signedJWT.getJWTClaimsSet().getJWTID());
        if(invalidtedToken.isPresent())
            throw new RuntimeException("Token expired time");

        String email = signedJWT.getJWTClaimsSet().getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

}
