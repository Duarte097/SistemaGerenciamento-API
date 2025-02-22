package com.example.sistema_gerenciamento_api.dto.lancamentoHorasDTO;

import java.time.LocalDateTime;

public record LancamentoHorasDTO(String nomeProjeto, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim) {
    
}
