package com.softuni.mehana.orderAPI.service.implementation;

import com.softuni.mehana.orderAPI.service.JwtServiceAPI;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Service
public class JwtServiceAPIImpl implements JwtServiceAPI {

    private final String jwtSecret;

    public JwtServiceAPIImpl(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public UserDetails extractUserInformation(String jwtToken) {
        Claims claims = extractClaims(jwtToken);

        String userName = getUserName(claims);
        List<String> roles = getRoles(claims);

        return new User(userName, "", roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
    }

    @SuppressWarnings("unchecked")
    private static List<String> getRoles(Claims claims) {
        return claims.get("roles", List.class);
    }

    private static String getUserName(Claims claims) {
        return claims.getSubject();
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
