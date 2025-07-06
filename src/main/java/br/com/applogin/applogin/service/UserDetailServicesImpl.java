package br.com.applogin.applogin.service;

import br.com.applogin.applogin.model.Usuario;
import br.com.applogin.applogin.model.UsuarioRole;
import br.com.applogin.applogin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException; // <-- IMPORTANTE ADICIONAR ESTE IMPORT
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServicesImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email);
        }

        // --- NOVA LÓGICA DE BLOQUEIO ---
        // Se o usuário encontrado for um técnico, bloqueia o acesso via web.
        if (usuario.getRole() == UsuarioRole.TECNICO) {
            // Lançamos uma exceção que o Spring Security entende como "login inválido".
            throw new DisabledException("Acesso web não permitido para técnicos. Utilize o aplicativo desktop.");
        }
        // --- FIM DA NOVA LÓGICA ---

        // Cria uma permissão (autoridade) baseada no perfil do usuário.
        // O Spring Security exige o prefixo "ROLE_".
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name());

        // Retorna um objeto User do Spring Security, agora com o perfil correto.
        return new User(usuario.getEmail(), usuario.getSenha(), Collections.singletonList(authority));
    }
}