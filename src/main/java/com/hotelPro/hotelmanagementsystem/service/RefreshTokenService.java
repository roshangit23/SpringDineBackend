package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.RefreshToken;
import com.hotelPro.hotelmanagementsystem.model.User;
import com.hotelPro.hotelmanagementsystem.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Transactional
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = new User();
        user.setId(userId);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new CustomException("Refresh token was expired. Please make a new signin request", HttpStatus.BAD_REQUEST);
        }
        return token;
    }
    @Transactional
    public void deleteByUserId(Long id) {
        refreshTokenRepository.deleteByUserId(id);
    }
    @Transactional
    public void deleteByToken(String token) {
        RefreshToken existingToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("RefreshToken", "token", token));;

        refreshTokenRepository.deleteByToken(token);

    }
}

