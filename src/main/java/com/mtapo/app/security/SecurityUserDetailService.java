package com.mtapo.app.security;

import com.mtapo.app.entity.User;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public SecurityUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(email);

        if (!user.isPresent())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        return new SecurityUserPrincipal(user.get());
    }
}
