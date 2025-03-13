package com.example.sistema_gerenciamento_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sistema_gerenciamento_api.entity.Atividade;


@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {
    List<Atividade> findByNomeAtividadeContaining(String nomeAtividade);
    @Query("SELECT a FROM Atividade a WHERE a.user.id_usuarios = :id")
    List<Atividade> findByUsuarioId(@Param("id") UUID id);

    @Query("SELECT a FROM Atividade a WHERE a.status = 'EM_ANDAMENTO' OR a.status = 'ABERTA'")
    List<Atividade> findAtividadesEmAndamentoOuAberta();
}

