package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        User usuarioResponsavel = getUserById(userId);
        
        // Verifica o perfil do usuário
        if (!usuarioResponsavel.getPerfil().equals("ADMIN")) {
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

    // Método para obter o usuário por ID
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    public List<Projeto> listProjetos(){
        return projetoRepository.findAll();
    }

    public void updateProjetoDto(String projetoId, ProjetoDTO updateProjetoDto){
        var projetoExists = projetoRepository.findById(UUID.fromString(projetoId));    

        if(projetoExists.isPresent()){
            var projetoEntity = projetoExists.get();

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

    public void deleteById(String projetoId){
        var projetoExists = projetoRepository.existsById(UUID.fromString(projetoId));

        if(projetoExists){
            userRepository.deleteById(UUID.fromString(projetoId));
        }else{
            throw new RuntimeException("User not found");
        }
    }
    
}
