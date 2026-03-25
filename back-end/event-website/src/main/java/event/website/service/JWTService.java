package event.website.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JWTService {
    private static final String SECRET = "53ebec311d355fb644065ec2e50ff80939781562aeb2e1f39825398575c8a302";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private static final Set<String> blacklistedTokens = new HashSet<>(); // In-memory blacklist

    public String generateToken(Authentication authentication) {
       List<String> roles=new LinkedList<>();
       for(GrantedAuthority authority:authentication.getAuthorities()){
           roles.add(authority.getAuthority());
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
               // .claim("roles", authentication.getAuthorities())
        .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, String username) {
        Claims body = getClaimsFromToken(token);
        String tokenUsername = body.getSubject();
        boolean notExpired = body.getExpiration().after(new Date());
        return username.equals(tokenUsername) && notExpired;
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token); // Add token to the blacklist set
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token); // Check if the token is blacklisted
    }

}
