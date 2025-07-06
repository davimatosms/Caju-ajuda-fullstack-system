package br.com.applogin.applogin.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    // Extrai o email do usuário a partir do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Gera um novo token JWT para um usuário
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Define o "assunto" do token (o email do usuário)
                .issuedAt(new Date(System.currentTimeMillis())) // Data de criação
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira em 24 horas
                .signWith(getSigningKey()) // Assina com a nossa chave secreta
                .compact();
    }

    // Verifica se um token é válido para um determinado usuário
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Método genérico para extrair qualquer "claim" (informação) do token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Verifica se o token já expirou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrai a data de expiração do token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai todas as informações (claims) do token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Obtém a chave secreta usada para assinar o token
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}