package br.com.applogin.applogin.config;

import br.com.applogin.applogin.model.Usuario;
import br.com.applogin.applogin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    // --- CADEIA DE FILTROS PARA A API REST (STATELESS) ---
    @Bean
    @Order(1) // Define a prioridade desta cadeia
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Aplica esta cadeia APENAS para rotas que começam com /api/
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/tecnico/**").hasRole("TECNICO")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- CADEIA DE FILTROS PARA A APLICAÇÃO WEB (STATEFUL) ---
    @Bean
    @Order(2) // Prioridade menor, pega o que a primeira cadeia não pegou
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/**")) // Aplica ao resto
                .csrf(AbstractHttpConfigurer::disable) // Mantemos desabilitado para não ter que mexer nos forms de novo
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/cadastroUsuario", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    // --- BEANS COMPARTILHADOS ---

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuário não encontrado: " + email);
            }

            // Esta lógica agora funciona corretamente, pois só será aplicada
            // no fluxo de login da web (formLogin), e não no fluxo da API.
            if (usuario.getRole() == br.com.applogin.applogin.model.UsuarioRole.TECNICO) {
                throw new DisabledException("Acesso web não permitido para técnicos.");
            }

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name());
            return new User(usuario.getEmail(), usuario.getSenha(), Collections.singletonList(authority));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}