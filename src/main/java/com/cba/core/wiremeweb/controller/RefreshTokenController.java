package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.RefreshTokenResource;
import com.cba.core.wiremeweb.dto.TokenRefreshRequestDto;
import com.cba.core.wiremeweb.dto.TokenRefreshResponseDto;
import com.cba.core.wiremeweb.exception.TokenRefreshException;
import com.cba.core.wiremeweb.model.RefreshToken;
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

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final JwtUtils jwtUtils;
    private final JwtEncoder encoder;


    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequestDto request) throws Exception {

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenServiceImpl.findByToken(requestRefreshToken)
                .map(refreshTokenServiceImpl::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUserName(), encoder);
                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}

