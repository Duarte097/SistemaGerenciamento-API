package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sistema_gerenciamento_api.dto.projetoDTO.ProjetoDTO;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;

@Service
public class ProjetoService {
    private ProjetoRepository projetoRepository;
    private UserRepository userRepository;
    //private User user;

    public ProjetoService(ProjetoRepository projetoRepository, UserRepository userRepository) {
        this.projetoRepository = projetoRepository;
        this.userRepository = userRepository;
    }

    public UUID createProjeto(ProjetoDTO projetoDto, UUID userId) {
        User usuarioCriador = getUserById(userId); // Usuário que está criando o projeto
        User usuarioResponsavel = getUserById(projetoDto.idUsuario()); // Usuário responsável pelo projeto

        // Verifica se o usuário que está criando o projeto é ADMIN
        if (!usuarioCriador.getPerfil().equals("ADMIN")) {
            throw new RuntimeException("Apenas usuários com perfil ADMIN podem criar projetos");
        }

        var entity = new Projeto(
            null, 
            projetoDto.nomeProjeto(),
            projetoDto.descricao(),
            projetoDto.dataInicio(),
            projetoDto.dataFim(),
            projetoDto.status(),
            LocalDateTime.now(), // Alterado de Instant.now() para LocalDateTime.now()
            usuarioResponsavel,
            projetoDto.prioridade(),
            new ArrayList<>() // Passando uma lista vazia de atividades
        );

 
        var projetoSaved = projetoRepository.save(entity);
        return projetoSaved.getId_projeto();
    }

    public Optional<Projeto> getProjetoById(String projetoId) {
       return projetoRepository.findById(UUID.fromString(projetoId));
    }

    public List<Projeto> getProjetoByName(String nomeProjeto) {
        return projetoRepository.findByNomeProjetoContaining(nomeProjeto);
    }
    
    public List<Projeto> getProjetosByNameAndUserId(String nomeProjeto, UUID userId) {
        User usuario = getUserById(userId);
        if (usuario.getPerfil().equals("ADMIN")) {
            return projetoRepository.findByNomeProjetoContaining(nomeProjeto);
        } else {
            List<Projeto> projetos = projetoRepository.findByNomeProjetoContaining(nomeProjeto);
            return projetos.stream()
            .filter(projeto -> projeto.getUsuarioResponsavel().getId_usuarios().equals(userId))
            .collect(Collectors.toList());
        }
    }

    // Método para obter o usuário por ID
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    public List<Projeto> listProjetos(UUID userId) {
        User usuario = getUserById(userId);
        if (usuario.getPerfil().equals("ADMIN")) {
            return projetoRepository.findAll();
        } else {
            return projetoRepository.findByUsuarioId(userId);
        }
    }

    public List<Projeto> getProjetosEmAndamento() {
        return projetoRepository.findProjetosEmAndamento();
    }
    
    public void updateProjetoDto(String projetoId, ProjetoDTO updateProjetoDto, UUID userId) {
        var projetoExists = projetoRepository.findById(UUID.fromString(projetoId));
        User usuario = getUserById(userId);

        if (projetoExists.isPresent()) {
            var projetoEntity = projetoExists.get();

            if (usuario.getPerfil().equals("USER") && !projetoEntity.getUsuarioResponsavel().getId_usuarios().equals(userId)) {
                throw new RuntimeException("Você não tem permissão para editar este projeto.");
            }
            if(updateProjetoDto.nomeProjeto() != null){
                projetoEntity.setNomeProjeto(updateProjetoDto.nomeProjeto());
            }
            if(updateProjetoDto.descricao() != null){
                projetoEntity.setDescricao(updateProjetoDto.descricao());
            }
            if(updateProjetoDto.dataInicio() != null){
                projetoEntity.setDataInicio(updateProjetoDto.dataInicio());
            }
            if(updateProjetoDto.dataFim() != null){
                projetoEntity.setDataFim(updateProjetoDto.dataFim());
            }
            if(updateProjetoDto.status() != null){
                projetoEntity.setStatus(updateProjetoDto.status());
            }
            if(updateProjetoDto.prioridade() != null){
                projetoEntity.setPrioridade(updateProjetoDto.prioridade());
            }
            projetoRepository.save(projetoEntity);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void deleteById(String projetoId, UUID userId) {
        var projetoExists = projetoRepository.findById(UUID.fromString(projetoId));
        User usuario = getUserById(userId);

        if (projetoExists.isPresent()) {
            var projetoEntity = projetoExists.get();
            if (usuario.getPerfil().equals("USER") && !projetoEntity.getUsuarioResponsavel().getId_usuarios().equals(userId)) {
                throw new RuntimeException("Você não tem permissão para deletar este projeto.");
            }
            projetoRepository.deleteById(UUID.fromString(projetoId));
        } else {
            throw new RuntimeException("Projeto não encontrado");
        }
    }
    
}
