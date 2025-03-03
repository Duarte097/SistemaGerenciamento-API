package com.example.sistema_gerenciamento_api.controllers;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.projetoDTO.ProjetoDTO;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.ProjetoService;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/projetos")
public class ProjetoController {
    private final ProjetoService projetoService;
    private final ProjetoRepository projetoRepository;
    private final UserRepository userRepository;
    

    public ProjetoController(ProjetoService projetoService, ProjetoRepository projetoRepository, UserRepository userRepository) {
        this.projetoService = projetoService;
        this.projetoRepository = projetoRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createProjeto(@RequestBody ProjetoDTO projetoDTO) {
        // Extrai o token JWT do usuário logado
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Jwt jwt = (Jwt) authentication.getPrincipal();*/
        // Supondo que o ID do usuário esteja armazenado na claim "sub"
        UUID userId;
        try {
            userId = projetoDTO.idUsuario();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<User> usuarioOptional = userRepository.findById(projetoDTO.idUsuario());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        User user = usuarioOptional.get();

        // Chama o serviço para criar o projeto e obter o ID criado
        UUID projetoId = projetoService.createProjeto(projetoDTO, userId);

        // Cria a entidade Projeto e a preenche com os dados
        Projeto projeto = new Projeto();
        projeto.setId_projeto(projetoId);
        projeto.setNomeProjeto(projetoDTO.nomeProjeto());
        projeto.setDescricao(projetoDTO.descricao());
        projeto.setDataInicio(projetoDTO.dataInicio());
        projeto.setDataFim(projetoDTO.dataFim());
        projeto.setStatus(projetoDTO.status());
        projeto.setPrioridade(projetoDTO.prioridade());
        projeto.setUsuarioResponsavel(user);

        // Salva a entidade no repositório
        projetoRepository.save(projeto);

        // Retorna a resposta (você pode optar por retornar 201 Created com a URI do recurso)
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listProjetos(){
        var projeto = projetoService.listProjetos();

        return ResponseEntity.ok(projeto);
    }

    @GetMapping("/{projetoId}")
    public ResponseEntity<Projeto> getProjetoById(@PathVariable String projetoId) {
        Optional<Projeto> projeto = projetoService.getProjetoById(projetoId);
        return projeto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = "nomeProjeto")
    public ResponseEntity<List<Projeto>> getProjetoByName(@RequestParam String nomeProjeto) {
        List<Projeto> projetos = projetoService.getProjetoByName(nomeProjeto);
        if (projetos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projetos);
    }
    
    @PutMapping("/{projetoId}")
    public ResponseEntity<Void> updateById(@PathVariable String projetoId,
                                           @RequestBody ProjetoDTO updateProjetoDto) {
        projetoService.updateProjetoDto(projetoId, updateProjetoDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projetoId}")
    public ResponseEntity<Void> deleteById(@PathVariable String projetoId) {
        projetoService.deleteById(projetoId);
        return ResponseEntity.noContent().build();
    }
}
