package com.tp.tp2authentificationavecjwt.controller;

import com.tp.tp2authentificationavecjwt.service.AuthRequest;
import com.tp.tp2authentificationavecjwt.service.AuthResponse;
import com.tp.tp2authentificationavecjwt.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService uds) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        var auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authenticationManager.authenticate(auth);
        UserDetails user = userDetailsService.loadUserByUsername(req.username());
        String token = jwtService.generateToken(user.getUsername(), Map.of("roles", user.getAuthorities()));
        return new AuthResponse(token);
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Bonjour, endpoint protégé OK ");
    }
}
