package com.example.reconstruction_2.config.filter;

import com.example.reconstruction_2.tolls.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwt;

    public SecurityFilter(JwtTokenProvider jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("AccessToken");
        String refreshToken = request.getHeader("RefreshToken");

        try {
            if (jwt.validateAccessToken(accessToken) || jwt.validateRefreshToken(refreshToken)) {
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                jwt.getUsername(refreshToken),
                                "",
                                Collections.singleton(
                                        (GrantedAuthority) () -> "USER")
                        );
                log.debug("User authenticated");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.warn("Request failed");
        }
        filterChain.doFilter(request, response);
    }
}