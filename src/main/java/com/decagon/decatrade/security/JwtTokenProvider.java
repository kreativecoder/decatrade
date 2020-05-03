package com.decagon.decatrade.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.decagon.decatrade.utils.Constants.EXPIRATION_TIME;
import static com.decagon.decatrade.utils.Constants.SECRET;
import static com.decagon.decatrade.utils.Constants.TOKEN_PREFIX;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    }

    public String getUsernameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, ""))
            .getSubject();
    }
}
