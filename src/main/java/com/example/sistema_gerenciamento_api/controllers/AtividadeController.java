package com.example.sistema_gerenciamento_api.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.atividadeDTO.AtividadeDTO;
import com.example.sistema_gerenciamento_api.dto.atividadeDTO.AtividadeSemLancamentoHorasDTO;
import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.AtividadeRepository;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.AtividadeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/atividades")
public class AtividadeController {
    private final AtividadeService atividadeService;
    private final AtividadeRepository atividadeRepository;
    private final UserRepository userRepository;
    private final ProjetoRepository projetoRepository;

    public AtividadeController(AtividadeService atividadeService, AtividadeRepository atividadeRepository
                               , UserRepository userRepository, ProjetoRepository projetoRepository) {
        this.atividadeService = atividadeService;
        this.atividadeRepository = atividadeRepository;
        this.userRepository = userRepository;
        this.projetoRepository = projetoRepository;     
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createAtividade(@RequestBody AtividadeDTO atividadeDTO) {
        UUID userId;
        UUID projetoId;
        try {
            userId = atividadeDTO.idUsuario();
            projetoId = atividadeDTO.idProjeto(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var atividadeId = atividadeService.createAtividade(atividadeDTO, userId, projetoId);
        if (atividadeId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar atividade.");
        }

        Optional<User> usuarioOptional = userRepository.findById(atividadeDTO.idUsuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
        User user = usuarioOptional.get();

        // Buscar o projeto no banco de dados
        Optional<Projeto> projetoOptional = projetoRepository.findById(atividadeDTO.idProjeto());
        if (projetoOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Projeto não encontrado.");
        }
        Projeto projeto = projetoOptional.get();

        // Criação da Atividade
        Atividade atividade = new Atividade();
        atividade.setId_atividade(atividadeId);
        atividade.setNomeAtividade(atividadeDTO.nomeAtividade());
        atividade.setDescricao_atividade(atividadeDTO.descricao());
        atividade.setDataInicio(atividadeDTO.dataInicio());
        atividade.setDataFim(atividadeDTO.dataFim());
        atividade.setStatus(atividadeDTO.status());
        atividade.setProjeto(projeto);
        atividade.setUser(user);
        atividade.setDataCriacao(LocalDateTime.now());

        // Salva a atividade no repositório
        atividadeRepository.save(atividade);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<AtividadeSemLancamentoHorasDTO>> listAtividade() {
        List<Atividade> atividades = atividadeService.listAtividades();
        List<AtividadeSemLancamentoHorasDTO> atividadesDTO = atividades.stream()
                .map(atividade -> new AtividadeSemLancamentoHorasDTO(
                        atividade.getId_atividade(),
                        atividade.getNomeAtividade(),
                        atividade.getDescricao_atividade(),
                        atividade.getDataInicio(),
                        atividade.getDataFim(),
                        atividade.getStatus(),
                        atividade.getUser().getId_usuarios(),// Supondo que User tenha um getId()
                        atividade.getProjeto().getId_projeto()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(atividadesDTO);
    }


    @GetMapping("/{atividadeId}")
    public ResponseEntity<Atividade> getAtividadeById(@PathVariable String atividadeId) {
        Optional<Atividade> atividade = atividadeService.getAtividadeById(atividadeId);
        return atividade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{atividadeId}")
    public ResponseEntity<Void> updateAtividadeById(@PathVariable String atividadeId,
                                            @RequestBody AtividadeDTO updateAtividadeDto) {
        try {
            atividadeService.updateAtividadeDto(atividadeId, updateAtividadeDto);
            return ResponseEntity.noContent().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    public void deleteById(String atividadeId) {
        var atividadeExists = atividadeRepository.existsById(UUID.fromString(atividadeId));
        if (atividadeExists) {
            atividadeRepository.deleteById(UUID.fromString(atividadeId));
        } else {
            throw new RuntimeException("Activity not found");
        }
    }
}

