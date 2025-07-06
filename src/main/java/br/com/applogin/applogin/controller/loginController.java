package br.com.applogin.applogin.controller;

import br.com.applogin.applogin.model.Usuario;
import br.com.applogin.applogin.model.UsuarioRole;
import br.com.applogin.applogin.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class loginController {

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/")
    public String dashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Usuario usuario = ur.findByEmail(userEmail);

        String primeiroNome = "Usuário";
        if (usuario != null && usuario.getNome() != null && !usuario.getNome().isBlank()) {
            primeiroNome = usuario.getNome().split(" ")[0];
        }
        model.addAttribute("nomeUsuario", primeiroNome);

        return "index";
    }

    @GetMapping("/cadastroUsuario")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastroUsuario")
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes){
        if (ur.findByEmail(usuario.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error_message", "Este e-mail já está em uso. Por favor, utilize outro.");
            return "redirect:/cadastroUsuario";
        }
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("error_message", "Ocorreram erros de validação.");
            return "redirect:/cadastroUsuario";
        }
        usuario.setRole(UsuarioRole.CLIENTE);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        ur.save(usuario);
        redirectAttributes.addFlashAttribute("success_message", "Conta criada com sucesso! Faça o login.");
        return "redirect:/login";
    }
}