package com.auth.controller;


import com.auth.dto.AuthResponse;
import com.auth.dto.*;
import com.auth.model.User;
import com.auth.config.JwtService;
import com.auth.service.CustomUserDetailsService;
import com.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager am,
                          CustomUserDetailsService uds,
                          JwtService js,
                          UserService us) {
        this.authManager = am;
        this.userDetailsService = uds;
        this.jwtService = js;
        this.userService = us;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(
                userDetails.getUsername(),
                Map.of("roles", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toSet()))
        );

        AuthResponse res = new AuthResponse();
        res.setAccessToken(token);
        res.setUserId(user.getId());
        res.setEmail(user.getEmail());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setRoles(userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet()));
        return res;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        authManager.authenticate(authToken);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtService.generateToken(
                userDetails.getUsername(),
                Map.of("roles", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toSet()))
        );

        AuthResponse res = new AuthResponse();
        res.setAccessToken(token);
        res.setEmail(userDetails.getUsername());
        res.setRoles(userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet()));

        // optionally set userId/firstName/lastName by reloading User entity
        return res;
    }
}