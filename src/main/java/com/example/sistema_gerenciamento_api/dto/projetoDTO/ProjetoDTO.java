package com.example.sistema_gerenciamento_api.dto.projetoDTO;

import java.time.LocalDateTime;

public record ProjetoDTO(String nome_projeto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, String status, String prioridade) {

}