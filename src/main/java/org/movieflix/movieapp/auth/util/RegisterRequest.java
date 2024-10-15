package org.movieflix.movieapp.auth.util;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RegisterRequest {

    private String name;
    private String username;
    private String email;
    private String password;
}
