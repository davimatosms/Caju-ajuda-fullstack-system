package br.com.applogin.applogin.controller;

import br.com.applogin.applogin.model.Usuario;
import br.com.applogin.applogin.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class loginController {

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private PasswordEncoder passwordEncoder; // INJETAR O ENCODER

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/")
    public String dashboard (){
        // A página 'index.html' será renderizada aqui
        return "index";
    }


    @GetMapping("/cadastroUsuario")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastroUsuario")
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return "redirect:/cadastroUsuario";
        }

        // Codificar a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        ur.save(usuario);

        return "redirect:/login";
    }
}