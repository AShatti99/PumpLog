package com.pumplog.PumpLog.config.jwt;

import com.pumplog.PumpLog.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtUtils {

    private final String SECRET = "kB+11g3U2V+H4A7nL2b6T5w1Z3U1vK8mYq5H7z9I4lQ=";                       // chiave usata per firmare e verificare i JWT
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());                         // crea una istanza di SecretKey usando la chiave segreta e l'algoritmo HS256

    public JwtDTO generateToken(String username) {
        Date createdAt = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(createdAt.getTime() + 86_400_000);

        String token = Jwts
                .builder()
                .subject(username)                                                                      // imposta l'utente come proprietà del token
                .issuedAt(createdAt)                                                                    // imposta la data di creazione del token
                .expiration(expiresAt)                                                                  // imposta la data di scadenza del token
                .signWith(SECRET_KEY)                                                                   // firma il token con la chiave segreta
                .compact();                                                                             // restituisci il token JWT come una stringa

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setToken(token);
        jwtDTO.setCreatedAt(createdAt);
        jwtDTO.setExpiresAt(expiresAt);

        return jwtDTO;
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
