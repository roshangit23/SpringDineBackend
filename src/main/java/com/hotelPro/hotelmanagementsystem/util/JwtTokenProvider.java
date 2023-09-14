package com.hotelPro.hotelmanagementsystem.util;

import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.User;
import com.hotelPro.hotelmanagementsystem.repository.UserRepository;
import com.hotelPro.hotelmanagementsystem.service.CustomUserDetails;
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
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1 hour

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserDetails customUserDetails;
    @Autowired
    private UserRepository userRepository;
    public String createToken(String username, Long companyId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("companyId", companyId);
        List<String> roles = getRolesByUsername(username);  // Fetch roles based on username
        claims.put("roles", roles);  // Add roles to the token
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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        Claims claims = Jwts.claims().setSubject(username);
        List<String> roles = getRolesByUsername(username);  // Fetch roles based on username
        claims.put("roles", roles);  // Add roles to the token

        // Check if the user is a dashboard user
        if (roles.contains("DASHBOARD_USER")) {
            // Fetch the companyIds for the dashboard user
            List<Long> companyIds = customUserDetails.getCompanies().stream()
                    .map(Company::getId)
                    .collect(Collectors.toList());
            claims.put("companyIds", companyIds);
        } else {
            // Fetch the companyId for the user
            Long companyId = user.getCompany().getId(); // You'll need to implement logic to fetch companyId for the user
            claims.put("companyId", companyId);
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
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

    private List<String> getRolesByUsername(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String createDashboardToken(String username, List<Long> companyIds) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("companyIds", companyIds);
        List<String> roles = getRolesByUsername(username);  // Fetch roles based on username
        claims.put("roles", roles);  // Add roles to the token
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


}

