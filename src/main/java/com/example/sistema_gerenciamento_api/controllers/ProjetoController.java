package com.example.sistema_gerenciamento_api.controllers;


import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.projetoDTO.ProjetoDTO;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.service.ProjetoService;



@RestController
@RequestMapping("/createProjeto")
public class ProjetoController {
    private final ProjetoService projetoService;
    private final ProjetoRepository projetoRepository;
    

    public ProjetoController(ProjetoService projetoService, ProjetoRepository projetoRepository) {
        this.projetoService = projetoService;
        this.projetoRepository = projetoRepository;
    }

    @PostMapping("/projetos")
    @Transactional
    public ResponseEntity<Void> createProjeto(@RequestBody ProjetoDTO projetoDTO) {
        // Extrai o token JWT do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // Supondo que o ID do usuário esteja armazenado na claim "sub"
        UUID userId;
        try {
            userId = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Chama o serviço para criar o projeto e obter o ID criado
        UUID projetoId = projetoService.createProjeto(projetoDTO, userId);

        // Cria a entidade Projeto e a preenche com os dados
        Projeto projeto = new Projeto();
        projeto.setId_projeto(projetoId);
        projeto.setNome_projeto(projetoDTO.nome_projeto());
        projeto.setDescricao(projetoDTO.descricao());
        projeto.setDataInicio(projetoDTO.dataInicio());
        projeto.setDataFim(projetoDTO.dataFim());
        projeto.setStatus(projetoDTO.status());
        projeto.setPrioridade(projetoDTO.prioridade());
        
        // Caso haja outros atributos para setar (como lista de atividades), configure aqui

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
}
