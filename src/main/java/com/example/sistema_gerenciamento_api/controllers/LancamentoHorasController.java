package com.example.sistema_gerenciamento_api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import com.example.sistema_gerenciamento_api.dto.lancamentoHorasDTO.LancamentoHorasDTO;
import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.LancamentoHoras;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.AtividadeRepository;
import com.example.sistema_gerenciamento_api.repository.LancamentoHorasRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.LacamentoHorasService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/lancamentoHoras")
public class LancamentoHorasController {
    private final LacamentoHorasService lancamentoHorasService;
    private final LancamentoHorasRepository lancamentoHorasRepository;
    private final UserRepository userRepository;
    private final AtividadeRepository atividadeRepository;
    

    public LancamentoHorasController(LacamentoHorasService lancamentoHorasService, LancamentoHorasRepository lancamentoHorasRepository,
                                    UserRepository userRepository, AtividadeRepository atividadeRepository) {
        this.lancamentoHorasService = lancamentoHorasService;
        this.lancamentoHorasRepository = lancamentoHorasRepository;
        this.userRepository = userRepository;
        this.atividadeRepository = atividadeRepository;
    }

    @PostMapping
    @Transactional
      public ResponseEntity<String> createLancamentoHoras(@RequestBody LancamentoHorasDTO lancamentoHorasDTO) {
        // Extrai o userId do token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId = UUID.fromString(jwt.getSubject());

        UUID atividadeId;
        try {
            atividadeId = lancamentoHorasDTO.idAtividade();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<User> usuarioOptional = userRepository.findById(lancamentoHorasDTO.idUsuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
        User user = usuarioOptional.get();

        // Buscar o projeto no banco de dados
        Optional<Atividade> atividadeOptional = atividadeRepository.findById(lancamentoHorasDTO.idAtividade());
        if (atividadeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Projeto não encontrado.");
        }
        Atividade atividade = atividadeOptional.get();

        // Chama o serviço para criar o projeto e obter o ID criado
        UUID lancamentoHorasId = lancamentoHorasService.createLancamentoHoras(lancamentoHorasDTO, userId, atividadeId);

        // Cria a entidade Projeto e a preenche com os dados
        LancamentoHoras lancamentoHoras = new LancamentoHoras();
        lancamentoHoras.setId_lancamentos_horas(lancamentoHorasId);
        lancamentoHoras.setUser(user);
        lancamentoHoras.setAtividade(atividade);
        lancamentoHoras.setDescricao(lancamentoHorasDTO.descricao());
        lancamentoHoras.setDataInicio(lancamentoHorasDTO.dataInicio());
        lancamentoHoras.setDataFim(lancamentoHorasDTO.dataFim());
        lancamentoHoras.setDataLancamento(lancamentoHorasDTO.dataLancamento());

        // Salva a entidade no repositório
        lancamentoHorasRepository.save(lancamentoHoras);

        // Retorna a resposta (você pode optar por retornar 201 Created com a URI do recurso)
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LancamentoHoras>> listLancamentoHoras() {
        // Extrai o userId do token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId = UUID.fromString(jwt.getSubject());

        List<LancamentoHoras> lancamentoHoras = lancamentoHorasService.listLancamentoHoras(userId);
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
        // Extrai o userId do token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId = UUID.fromString(jwt.getSubject());

        lancamentoHorasService.updateLancamentoHorasDto(lancamentoHorasId, updateLancamentoHorasDto, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lancamentoHorasId}")
    public ResponseEntity<Void> deleteLancamentoHorasById(@PathVariable String lancamentoHorasId) {
        // Extrai o userId do token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId = UUID.fromString(jwt.getSubject());

        lancamentoHorasService.deleteById(lancamentoHorasId, userId);
        return ResponseEntity.noContent().build();
    }
}
