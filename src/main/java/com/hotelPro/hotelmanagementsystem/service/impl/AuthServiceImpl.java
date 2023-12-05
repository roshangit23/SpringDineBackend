package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.exception.InvalidEnumValueException;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.*;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.UserRepository;
import com.hotelPro.hotelmanagementsystem.service.AuthService;
import com.hotelPro.hotelmanagementsystem.service.CustomUserDetails;
import com.hotelPro.hotelmanagementsystem.service.MyUserDetailsService;
import com.hotelPro.hotelmanagementsystem.service.RefreshTokenService;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    private static final Set<User.Role> RESTRICTED_ROLES = Set.of(User.Role.ROLE_ADMIN,User.Role.ROLE_DASHBOARD_USER);
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
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DASHBOARD_USER"))){
            throw new CustomException("Cannot login using Dashboard's username", HttpStatus.BAD_REQUEST);
        }
        Subscription subscription = customUserDetails.getSubscription();
        if(subscription==null) {
            throw new CustomException("User not subscribed to a plan", HttpStatus.BAD_REQUEST);
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = subscription.getExpiryDate();  // assuming subscription is your Subscription object
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, expiryDate);
        System.out.println(daysRemaining);
        if (daysRemaining <= 0) {
            throw new CustomException("Subscription plan has expired", HttpStatus.BAD_REQUEST);
        }
        final String jwt = jwtTokenUtil.createToken(authenticationRequest.getUsername(), customUserDetails.getCompany().getId(),customUserDetails.getSubscription().getName().toString(),daysRemaining);

        // Delete any existing refresh token for the user
        refreshTokenService.deleteByUserId(customUserDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getId());
        String companyName = customUserDetails.getCompany().getName();
        List<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, refreshToken.getToken(), customUserDetails.getId(), customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getMobileNumber(), roles, companyName,subscription.getName().toString());
    }
    @Override
    @Transactional
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
        newUser.setFirstName(userRequestDTO.getFirstName());
        newUser.setLastName(userRequestDTO.getLastName());
        newUser.setEmail(userRequestDTO.getEmail());
        newUser.setMobileNumber(userRequestDTO.getMobileNumber());
        // Convert roles from String to Role enum and set them
        try {
            Set<User.Role> userRoles = userRequestDTO.getRoles().stream()
                    .map(roleStr -> User.Role.valueOf(roleStr.trim().toUpperCase()))
                    .collect(Collectors.toSet());
            //if (userRoles.stream().anyMatch(role -> RESTRICTED_ROLES.contains(role))) {
            if (userRoles.stream().anyMatch(RESTRICTED_ROLES::contains)) {
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
        newUser.setFirstName(userRequestForAdminDTO.getFirstName());
        newUser.setLastName(userRequestForAdminDTO.getLastName());
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
       final CustomUserDetails customUserDetails = (CustomUserDetails) myUserDetailsService.loadUserByUsername(existingUser.getUsername());
        // Check if the user is a dashboard user or admin user
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DASHBOARD_USER"))
        || customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new CustomException("Admin user or dashboard user cannot be deleted", HttpStatus.FORBIDDEN);
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
           //if (userRoles.stream().anyMatch(role -> RESTRICTED_ROLES.contains(role))) {
          if (userRoles.stream().anyMatch(RESTRICTED_ROLES::contains)) {
               throw new CustomException("Invalid role provided", HttpStatus.BAD_REQUEST);
           }
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

        for (CompanyRequestDTO.RestaurantSectionDTO sectionDTO : companyDTO.getRestaurantSections()) {
            RestaurantSection section = new RestaurantSection();
            section.setRestaurantType(sectionDTO.getRestaurantType());
            company.getRestaurantSections().add(section);
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
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRoles().stream().map(User.Role::name).collect(Collectors.toSet())
        )).collect(Collectors.toList());
    }

    //dashboard
    @Override
    public String registerDashboardUser(DashboardUserRequestDTO dashboardUserRequestDTO)  {
        if (userRepository.findByUsername(dashboardUserRequestDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUsername(dashboardUserRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(dashboardUserRequestDTO.getPassword()));
        newUser.setEmail(dashboardUserRequestDTO.getEmail());
        newUser.setMobileNumber(dashboardUserRequestDTO.getMobileNumber());
       // Fetch the companies and associate them with the user
        Set<Company> associatedCompanies = new HashSet<>(companyRepository.findAllById(dashboardUserRequestDTO.getCompanyIds()));
        if (associatedCompanies.size() != dashboardUserRequestDTO.getCompanyIds().size()) {
            throw new CustomException("Some companies not found", HttpStatus.BAD_REQUEST);
        }
        newUser.setCompanies(associatedCompanies);
        // Set the user role as DASHBOARD_USER
        Set<User.Role> roles = new HashSet<>();
        roles.add(User.Role.ROLE_DASHBOARD_USER);
        newUser.setRoles(roles);

        userRepository.save(newUser);

        return "Dashboard user registered successfully!";
    }

    @Override
    public String addCompanyToDashboardUser(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
        String username = user.getUsername();
        final CustomUserDetails customUserDetails = (CustomUserDetails) myUserDetailsService.loadUserByUsername(username);
        if (!customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DASHBOARD_USER"))){
            throw new CustomException("User is not a dashboard user", HttpStatus.BAD_REQUEST);
        }
        // Check if the user is already associated with the company
        if (user.getCompanies().contains(company)) {
            return "User is already associated with this company.";
        }

        user.getCompanies().add(company);
        userRepository.save(user);

        return "Company added to dashboard user successfully!";
    }

    @Override
    public String removeCompanyFromDashboardUser(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
        String username = user.getUsername();
        final CustomUserDetails customUserDetails = (CustomUserDetails) myUserDetailsService.loadUserByUsername(username);
        if (!customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DASHBOARD_USER"))){
           throw new CustomException("User is not a dashboard user", HttpStatus.BAD_REQUEST);
       }
        if (!user.getCompanies().contains(company)) {
            throw new CustomException("Company not associated with the user", HttpStatus.BAD_REQUEST);
        }

        user.getCompanies().remove(company);
        userRepository.save(user);

        return "Company removed from dashboard user successfully!";
    }

    @Override
    public JwtResponse dashboardLogin(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        final CustomUserDetails customUserDetails = (CustomUserDetails) myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Check if the user is a dashboard user
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DASHBOARD_USER"))) {
            List<Long> companyIds = customUserDetails.getCompanies().stream()
                    .map(Company::getId)
                    .collect(Collectors.toList());
            System.out.println("List Of CompanyIds: "+companyIds.toString());
            final String jwt = jwtTokenUtil.createDashboardToken(customUserDetails.getUsername(),companyIds);

            //Delete any existing refresh token for the user
            refreshTokenService.deleteByUserId(customUserDetails.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getId());
            List<String> roles = customUserDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new JwtResponse(jwt, refreshToken.getToken(), customUserDetails.getId(), customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getMobileNumber(), roles, null,null); // No single company name for dashboard users
        } else {
            // Existing logic for regular users
           // return this.login(authenticationRequest);
            throw new CustomException("Please login using dashboard's username", HttpStatus.BAD_REQUEST);
        }
    }

}

