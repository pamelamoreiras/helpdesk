package com.pamela.helpdesk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtill {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(final String email){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();

    }

    public boolean tokenValido(final String token) {
        final Claims claims = getClaims(token);
        if (claims != null){
            final String username = claims.getSubject();
            final Date expirationDate = claims.getExpiration();
            final Date now = new Date(System.currentTimeMillis());

            if(username != null && expirationDate != null && now.before(expirationDate)){
                return true;
            }
        }
        return false;
    }

    private Claims getClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        } catch (final Exception e){
            return null;
        }
    }

    public String getUsername(final String token) {
        final Claims claims = getClaims(token);
        if (claims != null){
            return claims.getSubject();
        }

        return null;
    }
}
