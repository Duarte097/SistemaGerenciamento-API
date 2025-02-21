package com.example.sistema_gerenciamento_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.Projeto;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
    Optional<Projeto> findByNomeAtividade(String nomeAtividade);

}

