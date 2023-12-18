package com.hotelPro.hotelmanagementsystem.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelPro.hotelmanagementsystem.exception.CustomErrorResponse;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = resolveToken(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Expired or invalid JWT token");
        }
     catch (ExpiredJwtException e) {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
    } catch (MalformedJwtException e) {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is malformed");
    } catch (SignatureException e) {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
    }
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
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
