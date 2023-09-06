package com.hotelPro.hotelmanagementsystem.util;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1 hour

    @Autowired
    private UserDetailsService userDetailsService;
    public String createToken(String username, Long companyId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("companyId", companyId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsernameFromToken(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        catch (UsernameNotFoundException e) {
            throw new CustomException("User not found", HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String token) throws IOException {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
           throw new CustomException("Expired or invalid JWT token", HttpStatus.BAD_REQUEST);
       }
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)  // Use your JWT secret key here
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Invalid JWT token", HttpStatus.BAD_REQUEST);
        }
    }

    public String generateTokenFromUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new CustomException("Username cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + validityInMilliseconds))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }catch (Exception e) {
            // You can log the exception here if you have a logging framework set up
            throw new CustomException("Error generating JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private Claims getAllClaimsFromToken(String token) {
        try{
        return Jwts.parser()
                .setSigningKey(secretKey) // 'secret' is your JWT secret key
                .parseClaimsJws(token)
                .getBody();
    }
    catch (JwtException | IllegalArgumentException e) {
        throw new CustomException("Invalid JWT token", HttpStatus.BAD_REQUEST);
    }
    }
    public <T> T getClaimFromToken(String token, String claimKey, Class<T> clazz) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claims.get(claimKey, clazz);
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Invalid JWT token", HttpStatus.BAD_REQUEST);
        }
    }

}

