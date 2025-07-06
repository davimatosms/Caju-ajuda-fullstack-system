package br.com.applogin.applogin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    private String nome;

    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    private String senha;

    // CAMPO DE PERFIL QUE ESTAVA FALTANDO
    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

    // GETTERS E SETTERS
    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // GETTER E SETTER PARA ROLE QUE ESTAVAM FALTANDO
    public UsuarioRole getRole() {
        return role;
    }

    public void setRole(UsuarioRole role) {
        this.role = role;
    }
}