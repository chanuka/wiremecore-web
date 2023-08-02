package com.cba.core.wiremeweb.filter;

import com.cba.core.wiremeweb.config.JwtConfig;
import com.cba.core.wiremeweb.controller.DeviceController;
import com.cba.core.wiremeweb.service.impl.TokenBlacklistServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final JwtConfig jwtConfig;
    private final TokenBlacklistServiceImpl tokenBlacklistServiceImpl;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        logger.debug("Logout function is called--");

        String authorizationHeader = "";
        String message = "";

        if ((authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader())) != null) {

            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim();

            try {
                if (tokenBlacklistServiceImpl.createBlacklistToken(token) == null)
                    message = "Token Already Blacklisted...";
                else {
                    message = "Token Successfully Blacklisted...";
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                message = "Token Already Expired...";
            }

            response.setStatus(HttpServletResponse.SC_OK);
            // Write a response message
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message = "No Token Found...";
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
