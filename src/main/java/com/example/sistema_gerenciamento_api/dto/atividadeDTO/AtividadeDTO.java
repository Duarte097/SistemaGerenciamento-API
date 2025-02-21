package com.example.sistema_gerenciamento_api.dto.atividadeDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record AtividadeDTO(String nomeAtividade, String descricao, LocalDateTime dataInicio, 
                           LocalDateTime dataFim, String status, UUID idUsuario, UUID idProjeto){
 
}
