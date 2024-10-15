package org.movieflix.movieapp.Controller;


import lombok.AllArgsConstructor;
import org.movieflix.movieapp.auth.entity.RefreshToken;
import org.movieflix.movieapp.auth.entity.User;
import org.movieflix.movieapp.auth.service.AuthService;
import org.movieflix.movieapp.auth.service.JwtService;
import org.movieflix.movieapp.auth.service.RefreshTokensService;
import org.movieflix.movieapp.auth.util.AuthResponse;
import org.movieflix.movieapp.auth.util.LoginRequest;
import org.movieflix.movieapp.auth.util.RefreshTokenRequest;
import org.movieflix.movieapp.auth.util.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final RefreshTokensService refreshTokensService;
    private final JwtService jwtService;


    //register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest userRegistrationRequest) {
        authService.register(userRegistrationRequest);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

     RefreshToken refreshToken= refreshTokensService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
    //if RefreshToken   is valid
      User user=  refreshToken.getUser();

     String accesstoken= jwtService.generateToken(user);
     return ResponseEntity.ok(AuthResponse.builder()
                     .accessToken(accesstoken)
                     .refreshToken(refreshToken.getRefreshToken())
                     .build()

     );
    }
}

