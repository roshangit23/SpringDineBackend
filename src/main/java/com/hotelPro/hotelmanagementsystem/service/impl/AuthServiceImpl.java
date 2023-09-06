package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.RefreshToken;
import com.hotelPro.hotelmanagementsystem.model.User;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.UserRepository;
import com.hotelPro.hotelmanagementsystem.service.AuthService;
import com.hotelPro.hotelmanagementsystem.service.CustomUserDetails;
import com.hotelPro.hotelmanagementsystem.service.MyUserDetailsService;
import com.hotelPro.hotelmanagementsystem.service.RefreshTokenService;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private static final Set<User.Role> RESTRICTED_ROLES = Set.of(User.Role.ROLE_ADMIN);
    @Override
    public JwtResponse login(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        final CustomUserDetails customUserDetails = (CustomUserDetails) myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.createToken(authenticationRequest.getUsername(), customUserDetails.getCompany().getId());

        // Delete any existing refresh token for the user
        refreshTokenService.deleteByUserId(customUserDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getId());
        String companyName = customUserDetails.getCompany().getName();
        List<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, refreshToken.getToken(), customUserDetails.getId(), customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getMobileNumber(), roles, companyName);
    }
    @Override
    public String registerUser(UserRequestDTO userRequestDTO, String token) throws Exception {
        Company company = companyRepository.findById(userRequestDTO.getCompanyId())
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.BAD_REQUEST));

        Long tokenCompanyId = jwtTokenUtil.getClaimFromToken(token, "companyId", Long.class);
        if (!company.getId().equals(tokenCompanyId)) {
            throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
        }
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setCompany(company);
        newUser.setUsername(userRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        newUser.setEmail(userRequestDTO.getEmail());
        newUser.setMobileNumber(userRequestDTO.getMobileNumber());
        // Convert roles from String to Role enum and set them
        try {
            Set<User.Role> userRoles = userRequestDTO.getRoles().stream()
                    .map(roleStr -> User.Role.valueOf(roleStr.trim().toUpperCase()))
                    .collect(Collectors.toSet());
            if (userRoles.stream().anyMatch(role -> RESTRICTED_ROLES.contains(role))) {
                throw new CustomException("Invalid role provided", HttpStatus.BAD_REQUEST);
            }
            newUser.setRoles(userRoles);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException("Role", "Invalid value for Role");
        }
        userRepository.save(newUser);

        return "User registered successfully! Please login.";
    }
    @Override
    public String registerAdmin(UserRequestForAdminDTO userRequestForAdminDTO) throws Exception {
        if (userRepository.findByUsername(userRequestForAdminDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        Company company = companyRepository.findById(userRequestForAdminDTO.getCompanyId())
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.BAD_REQUEST));
        newUser.setCompany(company);
        newUser.setUsername(userRequestForAdminDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequestForAdminDTO.getPassword()));
        newUser.setEmail(userRequestForAdminDTO.getEmail());
        newUser.setMobileNumber(userRequestForAdminDTO.getMobileNumber());

        // Set the user role as ADMIN
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.ROLE_ADMIN);
        newUser.setRoles(roles);

        userRepository.save(newUser);

        return "Admin user registered successfully!";
    }
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenUtil.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new CustomException("Refresh token is not in database!", HttpStatus.BAD_REQUEST));
    }
    @Override
    public String deleteUser(Long userId, String token) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        Long tokenCompanyId = jwtTokenUtil.getClaimFromToken(token, "companyId", Long.class);
        if (!existingUser.getCompany().getId().equals(tokenCompanyId)) {
            throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
        }

        userRepository.deleteById(userId);
        return "User deleted successfully!";
    }
    @Override
    public String updateRole(Long userId, Set<String> roles, String token) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Long tokenCompanyId = jwtTokenUtil.getClaimFromToken(token, "companyId", Long.class);
            if (!user.getCompany().getId().equals(tokenCompanyId)) {
                throw new CustomException("Unauthorized access to company data", HttpStatus.FORBIDDEN);
            }
       try
       {
            Set<User.Role> userRoles = roles.stream()
            .map(roleStr -> User.Role.valueOf(roleStr.trim().toUpperCase()))
            .collect(Collectors.toSet());
             user.setRoles(userRoles);
          }
      catch (IllegalArgumentException e) {
             throw new InvalidEnumValueException("Role array", "Invalid value for Role array");
         }
            userRepository.save(user);
            return "User role updated successfully!";
        } else {
            throw new CustomException("User not found!", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public String logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return "Logged out successfully";
    }
    @Override
    public String registerCompany(CompanyRequestDTO companyDTO) {
        if (companyRepository.findByName(companyDTO.getName()).isPresent()) {
            throw new CustomException("Company with the same name already exists.", HttpStatus.BAD_REQUEST);
        }

        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setCity(companyDTO.getCity());
        company.setState(companyDTO.getState());
        company.setCountry(companyDTO.getCountry());
        company.setPostalCode(companyDTO.getPostalCode());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setEmail(companyDTO.getEmail());
        if (companyDTO.getLogoUrl() != null && !companyDTO.getLogoUrl().trim().isEmpty()) {
            company.setLogoUrl(companyDTO.getLogoUrl());
        }

        if (companyDTO.getTaxIdentificationNumber() != null && !companyDTO.getTaxIdentificationNumber().trim().isEmpty()) {
            company.setTaxIdentificationNumber(companyDTO.getTaxIdentificationNumber());
        }


        companyRepository.save(company);

        return "Company registered successfully!";
    }
    @Override
    public List<UserResponseDTO> getUsersByCompany(Long companyId, String token) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyId", companyId));

        Long tokenCompanyId = jwtTokenUtil.getClaimFromToken(token, "companyId", Long.class);
        if (!tokenCompanyId.equals(companyId)) {
            throw new CustomException("You don't have permission to view users from this company.", HttpStatus.FORBIDDEN);
        }

        List<User> users = userRepository.findByCompanyId(companyId);

        return users.stream().map(user -> new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRoles().stream().map(User.Role::name).collect(Collectors.toSet())
        )).collect(Collectors.toList());
    }
}
