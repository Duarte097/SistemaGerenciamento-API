package com.example.sistema_gerenciamento_api.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuarios")
    private UUID id_usuarios;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @CreationTimestamp
    private Instant data_criacao;

    @Column
    private LocalDateTime ultimoLogin;

    public User(){

    }

    public User(UUID id_usuarios, String nome, String email, String senha, Instant data_criacao, LocalDateTime ultimoLogin) {
        this.id_usuarios = id_usuarios;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.data_criacao = data_criacao;
        this.ultimoLogin = ultimoLogin;
    }

    public UUID getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(UUID id_usuarios) {
        this.id_usuarios = id_usuarios;
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

    public Instant getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Instant data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) { 
        this.ultimoLogin = ultimoLogin;
    }

}
