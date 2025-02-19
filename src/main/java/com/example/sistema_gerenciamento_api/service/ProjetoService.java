package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.sistema_gerenciamento_api.dto.projetoDTO.ProjetoDTO;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.Role;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;

@Service
public class ProjetoService {
    private ProjetoRepository projetoRepository;
    private UserRepository userRepository;

    public ProjetoService(ProjetoRepository projetoRepository, UserRepository userRepository) {
        this.projetoRepository = projetoRepository;
        this.userRepository = userRepository;
    }

    public UUID createProjeto(ProjetoDTO projetoDto, UUID userId) {
        if (!isAdmin(userId)) {
            throw new RuntimeException("Apenas usuários admin podem criar projetos");
        }
        var entity = new Projeto(
            null, 
            projetoDto.nome_projeto(),
            projetoDto.descricao(),
            projetoDto.dataInicio(),
            projetoDto.dataFim(),
            projetoDto.status(),
            LocalDateTime.now(), // Alterado de Instant.now() para LocalDateTime.now()
            getUserById(userId),
            projetoDto.prioridade(),
            new ArrayList<>() // Passando uma lista vazia de atividades
        );

 
        var projetoSaved = projetoRepository.save(entity);
        return projetoSaved.getId_projeto();
    }

    @SuppressWarnings("unlikely-arg-type")
    private boolean isAdmin(UUID userId) {
        var user = getUserById(userId);
        return user.getRoles().contains(Role.Values.ADMIN); // Supondo que você tenha uma enum Role com um valor ADMIN
    }
    
    // Método para obter o usuário por ID
    private User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public List<Projeto> listProjetos(){
        return projetoRepository.findAll();
    }
    
}
