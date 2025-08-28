package com.ra.base_spring_boot.security.jwt;

import com.ra.base_spring_boot.repository.BlacklistedTokenRepository;
import com.ra.base_spring_boot.security.principal.UserDetailService;
import com.ra.base_spring_boot.security.principal.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);

            if (token != null && jwtProvider.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (blacklistedTokenRepository.existsByToken(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token đã bị vô hiệu hóa (logout)");
                    return;
                }
                String username = jwtProvider.getUsernameFromToken(token);

                UserPrincipal userPrincipal =
                        (UserPrincipal) userDetailService.loadUserByUsername(username);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (jwtProvider.isTokenNearExpiry(token, 5 * 60 * 1000)) {
                    String newToken = jwtProvider.generateToken(username);
                    response.setHeader("X-New-Token", newToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Xác thực thất bại: " + e.getMessage());
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
