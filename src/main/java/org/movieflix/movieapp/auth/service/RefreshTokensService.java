package org.movieflix.movieapp.auth.service;


import lombok.AllArgsConstructor;
import org.movieflix.movieapp.auth.entity.RefreshToken;
import org.movieflix.movieapp.auth.entity.User;
import org.movieflix.movieapp.auth.repository.RefreshTokenRepository;
import org.movieflix.movieapp.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokensService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            long refreshTokenTime = 30* 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiresTime(Instant.now().plusMillis(refreshTokenTime))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    //validation of RefreshToken
    public RefreshToken validateRefreshToken(String refreshToken) {

        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh token Not Found"));

        //validate refresh token expired or not
        if (refToken.getExpiresTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refToken;
    }
}


