package com.example.littleBank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "8+vXK/1n3SjhwuPPOcusTgWnqny5Z8BuZLkk7Llr0Dz8vzydeaKflDZWb2ymSGFabpKRcMd2jykeyjikY8YU+RNOs6xNUjhq1QBufLpgd0YexGi9Cc+Va+T+lpHBm6ZfEYuyG+5Zisu8J3Vn1DQzMsXpivsnX2dmNjBBcx8xKCmfTJmJx6j8SzeEOFVmj+ypISPsTO7tVJ6i8VORChncbZZC6lD9/Jbh8QYLY/3cwlcDgWEBOzL3oVmlFVW5QqScm8x2IU+9cv0PudkFSg5Yh+UEOY6E14o41aNf0MWuB/7vCrDaHAVQEt65yiwruSSYvq+q3yCMlTBAmMQkUA4dEg2ldmKIW9qr6D05qm2Lsag=";


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // ---> subject è l'username o la mail
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Metodo per verificare se il token JWT è valido
    public boolean isTokenValid(String token) {
        try {
            // Parse del token per ottenere il corpo (payload)
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY) // Chiave segreta utilizzata per firmare il token
                    .parseClaimsJws(token)
                    .getBody();

            // Verifica se il token è scaduto
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            // Se il token è scaduto, consideralo non valido
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaim, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000000L *6000*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();    // ---> genera il token
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // signinkey ---> chiave per firmare il token usata per creare la signature part del JWT
                .build()                       // viene usato l'algoritmo specificato nel jwt token
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Metodo per ottenere l'ID utente dal token JWT
    public String getUserEmailFromToken(String token) {
        // Parse del token per ottenere il corpo (payload)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY) // Chiave segreta utilizzata per firmare il token
                .parseClaimsJws(token)
                .getBody();

        // Estrai l'ID utente dal corpo del token
        String email = claims.getSubject();

        return email;
    }

}
