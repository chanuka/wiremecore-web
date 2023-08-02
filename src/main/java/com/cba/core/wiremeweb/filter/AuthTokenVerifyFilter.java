package com.cba.core.wiremeweb.filter;

import com.cba.core.wiremeweb.config.JwtConfig;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.exception.JwtTokenException;
import com.cba.core.wiremeweb.service.impl.TokenBlacklistServiceImpl;
import com.cba.core.wiremeweb.service.impl.UserPermissionServiceImpl;
import com.cba.core.wiremeweb.util.JwtUtils;
import com.cba.core.wiremeweb.util.UserBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthTokenVerifyFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;
    private final UserPermissionServiceImpl userPermissionServiceImpl;
    private final JwtDecoder decoder;
    private final UserBean userBean;
    private final TokenBlacklistServiceImpl tokenBlacklistServiceImpl;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenVerifyFilter filter is called--");

        String token = null;
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            logger.debug("JWT token header is missing--");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim();

            if (tokenBlacklistServiceImpl.isTokenBlacklisted(token)) {
                throw new JwtTokenException(token, "Token is Blacklisted..");
            }

            Jwt claimsJws = jwtUtils.validateJwtToken(token, decoder);

            String username = claimsJws.getSubject();
            userBean.setUsername(username); // set user data in request scope for db updating


            String[] resource = request.getRequestURI().split("\\/");
            boolean access = false;
            List<PermissionResponseDto> permissionDtoList = null;
            permissionDtoList = userPermissionServiceImpl.findAll();

            for (PermissionResponseDto permission : permissionDtoList) {
                if (permission.getResource().equals(resource[2])) {
                    if (request.getMethod().equals("GET") && permission.isReadd()) {
                        access = true;
                        break;
                    } else if (request.getMethod().equals("POST") && permission.isCreated()) {
                        access = true;
                        break;
                    } else if (request.getMethod().equals("PUT") && permission.isUpdated()) {
                        access = true;
                        break;
                    } else if (request.getMethod().equals("DELETE") && permission.isDeleted()) {
                        access = true;
                        break;
                    }
                }
            }

            if (!access) {
                logger.debug("Access is denied for the provided JWT token");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new JwtTokenException(token, "Access is Denied..");
            }


            var authorities = (List<Map<String, String>>) claimsJws.getClaim("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("role")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtTokenException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (SQLException se) {
            logger.error(se.getMessage());
            throw new JwtTokenException(token, "Error in the token processing..");
        }
        filterChain.doFilter(request, response);
    }
}
