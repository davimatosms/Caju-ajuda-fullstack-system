package br.com.applogin.applogin.controller;

import br.com.applogin.applogin.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // <-- IMPORTANTE ADICIONAR
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Os DTOs (LoginRequest e LoginResponse) continuam os mesmos
class LoginRequest {
    private String email;
    private String senha;
    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

class LoginResponse {
    private final String token;
    public LoginResponse(String token) { this.token = token; }
    // Getter
    public String getToken() { return token; }
}


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // Injetamos o UserDetailsService que já configuramos
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. A autenticação continua igual
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())
            );

            // 2. Se a autenticação passar, buscamos o UserDetails
            //    em vez da nossa entidade Usuario.
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // 3. Geramos o token usando o objeto UserDetails, que é o tipo correto.
            final String jwt = jwtService.generateToken(userDetails);

            // 4. Retornamos o token.
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (AuthenticationException e) {
            // 5. Em caso de falha, o comportamento é o mesmo.
            return ResponseEntity.status(401).body("E-mail ou senha inválidos.");
        }
    }
}