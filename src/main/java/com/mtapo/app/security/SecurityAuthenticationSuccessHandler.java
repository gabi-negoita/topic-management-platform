package com.mtapo.app.security;

import com.mtapo.app.entity.User;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        String email = authentication.getName();

        Optional<User> userOptional = userService.findByEmail(email);
        userOptional.ifPresent(user -> session.setAttribute("sessionUser", user));

        session.removeAttribute("email");

        response.sendRedirect("/home");
    }
}
