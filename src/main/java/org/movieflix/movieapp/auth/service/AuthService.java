package org.movieflix.movieapp.auth.service;


import org.movieflix.movieapp.auth.entity.User;
import org.movieflix.movieapp.auth.entity.UserRole;
import org.movieflix.movieapp.auth.repository.UserRepository;
import org.movieflix.movieapp.auth.util.AuthResponse;
import org.movieflix.movieapp.auth.util.LoginRequest;
import org.movieflix.movieapp.auth.util.RegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.movieflix.movieapp.auth.entity.User.*;

@Service

public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokensService refreshTokensService;
    private final AuthenticationManager authenticationManager;


    public AuthService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, JwtService jwtService, RefreshTokensService refreshTokensService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokensService = refreshTokensService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        User user = builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

     User savedUser=userRepository.save(user);

     //generate jwt token
        var accesToken=jwtService.generateToken(savedUser);
        var refreshToken=refreshTokensService.createRefreshToken(savedUser.getEmail());


        return AuthResponse.builder()
                .accessToken(accesToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
        // encrypt password
    }
//login
    public AuthResponse login(LoginRequest loginRequest) {

        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                        loginRequest.getPassword())

        );
        var user=userRepository.findByUsername(loginRequest.getEmail()).orElseThrow(()->new RuntimeException("username not found"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokensService.createRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

    }

}
