package org.movieflix.movieapp.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
@SequenceGenerator(name = "refresh_token_seq", sequenceName = "refresh_token_seq")
public class RefreshToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RefreshTokenId;
    private String refreshToken;
    private Instant expiresTime;

    @OneToOne
    private User user;



}
