package com.example.sistema_gerenciamento_api.entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tb_projetos")
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projeto")
    private UUID id_projeto;

    @Column(nullable = false, length = 100)
    private String nome_projeto;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false, length = 20)
    private String status;
    
    @CreationTimestamp
    private LocalDateTime data_criacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_usuarios", nullable = false)
    private User usuarioResponsavel;

    @Column(nullable = false, length = 10)
    private String prioridade;

    @OneToMany(mappedBy = "projeto")
    private List<Atividade> atividades;

    public Projeto(UUID id_projeto, String nome_projeto, String descricao, LocalDateTime dataInicio,
            LocalDateTime dataFim, String status, LocalDateTime data_criacao, User usuarioResponsavel   , String prioridade,
            List<Atividade> atividades) {
        this.id_projeto = id_projeto;
        this.nome_projeto = nome_projeto;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.data_criacao = data_criacao;
        this.usuarioResponsavel = usuarioResponsavel;
        this.prioridade = prioridade;
        this.atividades = atividades;
    }

    public UUID getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(UUID id_projeto) {
        this.id_projeto = id_projeto;
    }

    public String getNome_projeto() {
        return nome_projeto;
    }

    public void setNome(String nome_projeto) {
        this.nome_projeto = nome_projeto;
    }


    public void getDescricao(String descricao) {
        this.descricao = descricao;
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
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
    public User getUsuarioResponsavel() { 
        return usuarioResponsavel; 
    }
    public void setUsuarioResponsavel(User usuarioResponsavel) { 
        this.usuarioResponsavel = usuarioResponsavel; 
    }
    public LocalDateTime getDataCriacao() { 
        return data_criacao; 
    }
    public String getPrioridade() { 
        return prioridade; 
    }
    public void setPrioridade(String prioridade) { 
        this.prioridade = prioridade; 
    }
    public List<Atividade> getAtividades() { 
        return atividades; 
    }
    public void setAtividades(List<Atividade> atividades) { 
        this.atividades = atividades; 
    }

}