package com.example.sistema_gerenciamento_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sistema_gerenciamento_api.entity.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    List<Projeto> findByNomeProjetoContaining(String nomeProjeto);
    @Query("SELECT p FROM Projeto p WHERE p.idUsuario.id = :id")
    List<Projeto> findByUsuarioId(@Param("id") UUID id);

}