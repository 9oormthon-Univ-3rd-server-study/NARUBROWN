package me.na2ru2.narubrown.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.na2ru2.narubrown.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }

    public String createAccessToken(String user_email) {
        Claims claims = Jwts.claims().setSubject(user_email);
        claims.put("type", "access");
        Date now = new Date();
        long tokenValidMillisecond = 1000L * 60 * 30;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(this.getSigningKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByEmail(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        try {
            if (!token.substring(0,"BEARER ".length()).equalsIgnoreCase("Bearer ")) {
                throw new IllegalStateException("Token 정보가 존재하지 않습니다.");
            }
            token = token.split(" ")[1].trim();
        } catch (Exception e) {
            return null;
        }
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(this.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


}
