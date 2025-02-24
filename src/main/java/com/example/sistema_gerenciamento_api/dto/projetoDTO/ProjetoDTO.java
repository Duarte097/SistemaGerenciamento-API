package com.example.sistema_gerenciamento_api.dto.projetoDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjetoDTO(String nomeProjeto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, String status, String prioridade, UUID idUsuario) {

}