package com.example.system.Security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid email or password.";
        
        if (exception.getMessage().equalsIgnoreCase("User not found")) {
            errorMessage = "Email does not exist.";
        }

        // 將錯誤消息放入請求屬性
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + "/login?error=true");
    }
}
