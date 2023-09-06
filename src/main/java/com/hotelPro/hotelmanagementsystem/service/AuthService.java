package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;

import java.util.List;
import java.util.Set;

public interface AuthService {
    JwtResponse login(AuthenticationRequest authenticationRequest) throws Exception;

    String registerUser(UserRequestDTO userRequestDTO, String token) throws Exception;

    String registerAdmin(UserRequestForAdminDTO userRequestForAdminDTO) throws Exception;

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);

    String deleteUser(Long userId, String token);

    String updateRole(Long userId, Set<String> roles, String token);

    String logout(String refreshToken);

    String registerCompany(CompanyRequestDTO companyDTO);

    List<UserResponseDTO> getUsersByCompany(Long companyId, String token);
}
