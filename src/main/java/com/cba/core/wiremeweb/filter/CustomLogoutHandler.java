package com.cba.core.wiremeweb.filter;

import com.cba.core.wiremeweb.config.JwtConfig;
import com.cba.core.wiremeweb.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    private final JwtConfig jwtConfig;
    private final TokenBlacklistService tokenBlacklistService;
    private final MessageSource messageSource;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Locale currentLocale = LocaleContextHolder.getLocale();// works only when as local statement
        logger.debug(messageSource.getMessage("GLOBAL_LOGOUT_SERVER_DEBUG", null, currentLocale));

        String authorizationHeader = "";
        String message = "";

        if ((authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader())) != null) {

            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim();

            try {
                if (tokenBlacklistService.createBlacklistToken(token) == null)
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
