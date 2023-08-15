package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.RefreshTokenResource;
import com.cba.core.wiremeweb.dto.TokenRefreshRequestDto;
import com.cba.core.wiremeweb.dto.TokenRefreshResponseDto;
import com.cba.core.wiremeweb.exception.TokenRefreshException;
import com.cba.core.wiremeweb.model.TokenRefresh;
import com.cba.core.wiremeweb.service.RefreshTokenService;
import com.cba.core.wiremeweb.service.impl.RefreshTokenServiceImpl;
import com.cba.core.wiremeweb.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Component
@RequiredArgsConstructor
@Validated
public class RefreshTokenController implements RefreshTokenResource {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final JwtEncoder encoder;


    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequestDto request) throws Exception {

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(TokenRefresh::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUserName(), encoder);
                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}

