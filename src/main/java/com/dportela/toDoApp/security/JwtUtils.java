package com.dportela.toDoApp.security;

import com.dportela.toDoApp.models.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtTimeoutMs;

    @Value("${jwt.authorizationHeader}")
    private String authorizationHeader;

    @Value("${jwt.prefix}")
    private String jwtPrefix;

    public String generateToken(User user) {
        String username = user.getUsername();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtTimeoutMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Could not claim JWT: " + e.getMessage());
        }

        return false;
    }

    public String parseAuthorizationHeader(HttpServletRequest req) {
        String header = req.getHeader(authorizationHeader);

        if (StringUtils.hasText(header) && header.startsWith(jwtPrefix)) {
            return header.replace(jwtPrefix, "");
        }

        return null;
    }

    public String generateAuthorizationHeader(String token) {
        return jwtPrefix + token;
    }
}
