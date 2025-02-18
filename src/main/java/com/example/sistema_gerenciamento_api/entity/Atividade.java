package com.example.sistema_gerenciamento_api.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ativdade")
    private UUID id_atividade;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, length = 200)
    private String nome_atividade;

    @Column(columnDefinition = "TEXT")
    private String descricao_atividade;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_usuarios", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "atividade")
    private List<LancamentoHoras> subAtividades;

    public Atividade(UUID id_atividade, Projeto projeto, String nome_atividade, String descricao_atividade,
            LocalDateTime dataInicio, LocalDateTime dataFim, String status, User user, LocalDateTime dataCriacao,
            List<LancamentoHoras> subAtividades) {
        this.id_atividade = id_atividade;
        this.projeto = projeto;
        this.nome_atividade = nome_atividade;
        this.descricao_atividade = descricao_atividade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.user = user;
        this.dataCriacao = dataCriacao;
        this.subAtividades = subAtividades;
    }

    public UUID getId_atividade() {
        return id_atividade;
    }

    public void setId_atividade(UUID id_atividade) {
        this.id_atividade = id_atividade;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getNome_atividade() {
        return nome_atividade;
    }

    public void setNome_atividade(String nome_atividade) {
        this.nome_atividade = nome_atividade;
    }

    public String getDescricao_atividade() {
        return descricao_atividade;
    }

    public void setDescricao_atividade(String descricao_atividade) {
        this.descricao_atividade = descricao_atividade;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<LancamentoHoras> getSubAtividades() {
        return subAtividades;
    }

    public void setSubAtividades(List<LancamentoHoras> subAtividades) {
        this.subAtividades = subAtividades;
    }

}
