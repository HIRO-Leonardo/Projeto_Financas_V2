package com.DespesasFinanceiras.V1.Config;

import com.DespesasFinanceiras.V1.Entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    @Value("${PALAVRA_SECRETA}")
    private String secret;

    public String generateToken(UserEntity userEntity){

        Algorithm algorithm = Algorithm.HMAC256(secret);


        return JWT.create()
                .withClaim("userId", userEntity.getId())
                .withSubject(userEntity.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86000))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build().verify(token);
            return Optional.of(JWTUserData.builder()
                    .userId(decodedJWT.getClaim("userId").asLong())
                    .email(decodedJWT.getSubject())
                    .build());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }

    }
}
