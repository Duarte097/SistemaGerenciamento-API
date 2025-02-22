package com.example.sistema_gerenciamento_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sistema_gerenciamento_api.entity.LancamentoHoras;

public interface LancamentoHorasRepository extends JpaRepository<LancamentoHoras, UUID>  {
    //Optional<LancamentoHoras> findByNomeLancamentoHoras(String lancamentoHoras);

}
