package br.com.applogin.applogin.model;

import jakarta.persistence.*;

@Entity
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo; // Nome original que o usuário enviou
    private String tipoArquivo;   // Tipo MIME: "image/png", "application/pdf"
    private String nomeUnico;     // Nome gerado para salvar no disco

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;

    // --- GETTERS E SETTERS COMPLETOS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getNomeUnico() {
        return nomeUnico;
    }

    public void setNomeUnico(String nomeUnico) {
        this.nomeUnico = nomeUnico;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }
}