package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CompanyRepository companyRepository;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.login(authenticationRequest)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization").substring(7); // Assuming "Bearer " prefix
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.registerUser(userRequestDTO, token)));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<?>> registerAdminUser(@Valid @RequestBody UserRequestForAdminDTO userRequestForAdminDTO) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.registerAdmin(userRequestForAdminDTO)));
    }

    @PostMapping("/register-company")
    public ResponseEntity<ApiResponse<?>> registerCompany(@Valid @RequestBody CompanyRequestDTO companyDTO) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.registerCompany(companyDTO)));
    }

    @GetMapping("/users-by-company/{companyId}")
    public ResponseEntity<ApiResponse<?>> getUsersByCompany(@PathVariable Long companyId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Assuming "Bearer " prefix
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.getUsersByCompany(companyId, token)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.refreshToken(request)));
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Assuming "Bearer " prefix
        authService.deleteUser(userId, token);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully"));
    }

    @PutMapping("/updateRole/{userId}")
    public ResponseEntity<ApiResponse<?>> updateRole(@PathVariable Long userId, @Valid @RequestBody @NotEmpty Set<String> roles, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Assuming "Bearer " prefix
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.updateRole(userId, roles, token)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("X-Refresh-Token") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Logged out successfully"));
    }

    //dashboard
    @PostMapping("/register-dashboard-user")
    public ResponseEntity<ApiResponse<?>> registerDashboardUser(@Valid @RequestBody DashboardUserRequestDTO dashboardUserRequestDTO) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.registerDashboardUser(dashboardUserRequestDTO)));
    }

    @PostMapping("/dashboard-user/add-company/{userId}")
    public ResponseEntity<ApiResponse<?>> addCompanyToDashboardUser(@PathVariable Long userId, @Valid @RequestBody AddRemoveCompanyDTO addRemoveCompanyDTO) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.addCompanyToDashboardUser(userId, addRemoveCompanyDTO.getCompanyId())));
    }
    @PostMapping("/dashboard-user/remove-company/{userId}")
    public ResponseEntity<ApiResponse<?>> removeCompanyFromDashboardUser(@PathVariable Long userId, @Valid @RequestBody AddRemoveCompanyDTO addRemoveCompanyDTO) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.removeCompanyFromDashboardUser(userId, addRemoveCompanyDTO.getCompanyId())));
    }
    @PostMapping("/dashboard/login")
    public ResponseEntity<ApiResponse<?>> createDashboardAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), authService.dashboardLogin(authenticationRequest)));
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyId", companyId));
        CompanyResponseDTO response = new CompanyResponseDTO(company);
        return ResponseEntity.ok(response);
    }
}
