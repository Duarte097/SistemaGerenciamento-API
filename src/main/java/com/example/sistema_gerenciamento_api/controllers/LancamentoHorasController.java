package com.example.sistema_gerenciamento_api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.lancamentoHorasDTO.LancamentoHorasDTO;
import com.example.sistema_gerenciamento_api.entity.LancamentoHoras;
import com.example.sistema_gerenciamento_api.repository.LancamentoHorasRepository;
import com.example.sistema_gerenciamento_api.service.LacamentoHorasService;

@RestController
@RequestMapping("/lancamentoHoras")
public class LancamentoHorasController {
    private final LacamentoHorasService lancamentoHorasService;
    private final LancamentoHorasRepository lancamentoHorasRepository;
    

    public LancamentoHorasController(LacamentoHorasService lancamentoHorasService, LancamentoHorasRepository lancamentoHorasRepository) {
        this.lancamentoHorasService = lancamentoHorasService;
        this.lancamentoHorasRepository = lancamentoHorasRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createLancamentoHoras(@RequestBody LancamentoHorasDTO lancamentoHorasDTO) {
        // Extrai o token JWT do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // Supondo que o ID do usuário esteja armazenado na claim "sub"
        UUID userId;
        UUID atividadeId;
        try {
            userId = UUID.fromString(jwt.getSubject());
            atividadeId = UUID.fromString(jwt.getClaimAsString("atividadeId"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Chama o serviço para criar o projeto e obter o ID criado
        UUID lancamentoHorasId = lancamentoHorasService.createLancamentoHoras(lancamentoHorasDTO, userId, atividadeId);

        // Cria a entidade Projeto e a preenche com os dados
        LancamentoHoras lancamentoHoras = new LancamentoHoras();
        lancamentoHoras.setId_lancamentos_horas(lancamentoHorasId);
        lancamentoHoras.setUser(lancamentoHorasService.getUserById(userId));
        lancamentoHoras.setAtividade(lancamentoHorasService.getAtividadeById(atividadeId));
        lancamentoHoras.setDescricao(lancamentoHorasDTO.descricao());
        lancamentoHoras.setDataInicio(lancamentoHorasDTO.dataInicio());
        lancamentoHoras.setDataFim(lancamentoHorasDTO.dataFim());

        // Salva a entidade no repositório
        lancamentoHorasRepository.save(lancamentoHoras);

        // Retorna a resposta (você pode optar por retornar 201 Created com a URI do recurso)
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LancamentoHoras>> listLancamentoHoras(){
        var lancamentoHoras = lancamentoHorasService.listLancamentoHoras();

        return ResponseEntity.ok(lancamentoHoras);
    }

    @GetMapping("/{lancamentoHorasId}")
    public ResponseEntity<LancamentoHoras> getLancamentoHorasById(@PathVariable String lancamentoHorasId) {
        Optional<LancamentoHoras> lancamentoHoras = lancamentoHorasService.getLancamentoHorasById(lancamentoHorasId);
        return lancamentoHoras.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{lancamentoHorasId}")
    public ResponseEntity<Void> updateLanamentoHorasById(@PathVariable String lancamentoHorasId,
                                           @RequestBody LancamentoHorasDTO updateLancamentoHorasDto) {
        lancamentoHorasService.updateLancamentoHorasDto(lancamentoHorasId, updateLancamentoHorasDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lancamentoHorasId}")
    public ResponseEntity<Void> deleteLancamentoHorasById(@PathVariable String lancamentoHorasId) {
        lancamentoHorasService.deleteById(lancamentoHorasId);
        return ResponseEntity.noContent().build();
    }
}
