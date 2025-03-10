package com.example.sistema_gerenciamento_api.dto.lancamentoHorasDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record LancamentoHorasDTO(String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, UUID idUsuario, UUID idAtividade, LocalDateTime dataLancamento) {
    
}
