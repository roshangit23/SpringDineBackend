package com.hotelPro.hotelmanagementsystem.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelPro.hotelmanagementsystem.exception.CustomErrorResponse;
import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.service.CompanyAssociatedEntity;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CompanyIdFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${paths.with.companyId}")
    private List<String> pathsWithCompanyId;

    @Value("${paths.excluded}")
    private List<String> excludedPaths;

    @Autowired
    private EntityServiceResolver entityServiceResolver;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
try {
    String path = request.getRequestURI();
    // Check if the path is one of the excluded paths
    if (excludedPaths.stream().anyMatch(path::startsWith)) {
        filterChain.doFilter(request, response);
        return;
    }

    String companyIdFromPath = null;
    // Check if the path is one of those that contain companyId
    boolean isPathWithCompanyId = pathsWithCompanyId.stream().anyMatch(path::startsWith);

    if (isPathWithCompanyId) {
        // Extract companyId from path
        String[] pathParts = path.split("/");
        Long pathCompanyId = Long.parseLong(pathParts[3]); // Assuming companyId is always the third part of the path
        companyIdFromPath = pathCompanyId.toString();
    }
    String authHeader = request.getHeader("Authorization");
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
//        return;
//    }
    String token = authHeader.substring(7); // Assuming "Bearer " prefix
    Long tokenCompanyId = jwtTokenProvider.getClaimFromToken(token, "companyId", Long.class);

    if (companyIdFromPath != null) {
        if (!tokenCompanyId.toString().equals(companyIdFromPath)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to company data");
            return;
        }
    } else if (!isPathWithCompanyId && !excludedPaths.contains(path)) {
        List<CompanyAssociatedEntity> entities = entityServiceResolver.resolveEntity(request);

        // Check if the entities list is null or empty
        if (entities == null || entities.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request path or endpoint not configured");
            return;
        }

        for (CompanyAssociatedEntity entity : entities) {
            if (!tokenCompanyId.equals(entity.getCompany().getId())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to company data");
                return;
            }
        }
    }
    filterChain.doFilter(request, response);
}
//catch (ResourceNotFoundException ex) {
//    response.setStatus(HttpStatus.NOT_FOUND.value());
//    response.setContentType("application/json");
//
//    CustomErrorResponse errorResponse = new CustomErrorResponse();
//    errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
//    errorResponse.setError(ex.getMessage());
//
//    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//    response.getWriter().write(jsonResponse);
//    return;
//}

catch (NullPointerException e) {
    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid token or missing companyId claim");
    return;
} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid companyId in path");
    return;
}  catch (ExpiredJwtException e) {
    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
    return;
} catch (MalformedJwtException e) {
    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is malformed");
    return;
} catch (SignatureException e) {
    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
    return;
}catch (ResourceNotFoundException ex) {
    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
    return;
}
//catch (Exception e) {
//    sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
//    return;
//}
    }
    private void sendErrorResponse(HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(status);
        errorResponse.setError(errorMessage);

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}

