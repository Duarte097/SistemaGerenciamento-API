package com.example.sistema_gerenciamento_api.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.atividadeDTO.AtividadeDTO;

import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.AtividadeRepository;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.AtividadeService;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {
    private final AtividadeService atividadeService;
    private final AtividadeRepository atividadeRepository;
    private final UserRepository userRepository;
    private final ProjetoRepository projetoRepository;
    

    public AtividadeController(AtividadeService atividadeService, AtividadeRepository atividadeRepository, 
                                UserRepository userRepository, ProjetoRepository projetoRepository) {
        this.atividadeService = atividadeService;
        this.atividadeRepository = atividadeRepository;
        this.userRepository = userRepository;
        this.projetoRepository = projetoRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createAtividade(@RequestBody AtividadeDTO atividadeDTO) {
        // Extrai o token JWT do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId;
        UUID projetoId;
        try {
            userId = UUID.fromString(jwt.getSubject());
            projetoId = atividadeDTO.idProjeto(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Buscar projeto pelo ID
        Optional<Projeto> projeto = projetoRepository.findById(projetoId);
        if (projeto.isEmpty()) {
            return ResponseEntity.badRequest().body("Projeto não encontrado.");
        }

        Optional<User> usuarioResponsavel = userRepository.findById(userId);
        if (usuarioResponsavel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário responsável não encontrado.");
        }

        // Criação da Atividade
        Atividade atividade = new Atividade();
        atividade.setId_atividade(UUID.randomUUID());
        atividade.setNomeAtividade(atividadeDTO.nomeAtividade());
        atividade.setDescricao_atividade(atividadeDTO.descricao());
        atividade.setDataInicio(atividadeDTO.dataInicio());
        atividade.setDataFim(atividadeDTO.dataFim());
        atividade.setStatus(atividadeDTO.status());
        atividade.setProjeto(projeto.get());
        atividade.setUser(usuarioResponsavel.get());
        atividade.setDataCriacao(LocalDateTime.now());

        // Salva a atividade no repositório
        atividadeRepository.save(atividade);


        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<Atividade>> listAtividades(){
        var atividade = atividadeService.listAtividades();

        return ResponseEntity.ok(atividade);
    }

    /*@GetMapping("/{atividadeId}")
    public ResponseEntity<Atividade> getAtividadeById(@PathVariable String atividadeId) {
        Optional<Atividade> atividade = atividadeService.getAtividadeById(atividadeId);
        return atividade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{atividadeId}")
    public ResponseEntity<Void> updateById(@PathVariable String atividadeId,
                                           @RequestBody AtividadeDTO updateAtividadeDto) {
        atividadeService.updateAtividadeDto(atividadeId, updateAtividadeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{atividadeId}")
    public ResponseEntity<Void> deleteById(@PathVariable String atividadeId) {
        atividadeService.deleteById(atividadeId);
        return ResponseEntity.noContent().build();
    }*/
}

