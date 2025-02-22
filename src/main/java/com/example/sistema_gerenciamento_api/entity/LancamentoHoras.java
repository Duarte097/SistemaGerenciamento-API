package com.example.sistema_gerenciamento_api.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_lancamentos_horas")
public class LancamentoHoras {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_lancamentos_horas")
    private UUID id_lancamentos_horas;

    
    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataRegistro = LocalDateTime.now();

    public LancamentoHoras(){
        
    }

    public LancamentoHoras(UUID id_lancamentos_horas, Atividade atividade, User user, String descricao,
            LocalDateTime dataInicio, LocalDateTime dataFim, LocalDateTime dataRegistro) {
        this.id_lancamentos_horas = id_lancamentos_horas;
        this.atividade = atividade;
        this.user = user;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataRegistro = dataRegistro;
    }

    public UUID getId_lancamentos_horas() {
        return id_lancamentos_horas;
    }

    public void setId_lancamentos_horas(UUID id_lancamentos_horas) {
        this.id_lancamentos_horas = id_lancamentos_horas;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

}
