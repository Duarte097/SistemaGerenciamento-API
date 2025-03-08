package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.sistema_gerenciamento_api.dto.atividadeDTO.AtividadeDTO;
import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.Projeto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.AtividadeRepository;
import com.example.sistema_gerenciamento_api.repository.ProjetoRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;



@Service
public class AtividadeService {
        private AtividadeRepository atividadeRepository;
        private UserRepository userRepository;
        private ProjetoRepository projetoRepository;

    public AtividadeService(AtividadeRepository atividadeRepository,  UserRepository userRepository, ProjetoRepository projetoRepository) {
        this.atividadeRepository = atividadeRepository;
        this.userRepository = userRepository;
        this.projetoRepository = projetoRepository;
    }
    

    public UUID createAtividade(AtividadeDTO atividadeDto, UUID userId, UUID projetoId) {
        Projeto projeto = getProjetoById(projetoId);
        User usuarioResponsavelProjeto = projeto.getUsuarioResponsavel(); // Responsável pelo projeto
        User usuarioResponsavelAtividade = getUserById(atividadeDto.idUsuario()); // Responsável pela atividade

        // Verifica se o usuário que está criando a atividade é o responsável pelo projeto
        if (!usuarioResponsavelProjeto.getId_usuarios().equals(userId)) {
            throw new RuntimeException("Apenas o responsável pelo projeto pode criar atividades.");
        }

        var entity = new Atividade(
            null, 
            projeto, 
            atividadeDto.nomeAtividade(),             
            atividadeDto.descricao(), 
            atividadeDto.dataInicio(), 
            atividadeDto.dataFim(),
            atividadeDto.status(), 
            usuarioResponsavelAtividade,
            LocalDateTime.now(),
            null
        );

        var atividadeSaved = atividadeRepository.save(entity);
        return atividadeSaved.getId_atividade();
    }

    public User getUserById(UUID userId) {
        System.out.println("Buscando usuário com ID: " + userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Projeto getProjetoById(UUID projetoId) {
        System.out.println("Buscando projeto com ID: " + projetoId);
        return projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    
    public Optional<Atividade> getAtividadeById(String atividadeId) {
       return atividadeRepository.findById(UUID.fromString(atividadeId));
    }

    public List<Atividade> listAtividades(UUID userId) {
        User usuario = getUserById(userId);
        if (usuario.getPerfil().equals("ADMIN")) {
            return atividadeRepository.findAll();
        } else {
           return atividadeRepository.findByUsuarioId(userId);
        }
        
    }


    public void updateAtividadeDto(String atividadeId, AtividadeDTO updateAtividadeDto, UUID userId) {
        Atividade atividadeEntity = atividadeRepository.findById(UUID.fromString(atividadeId))
            .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Projeto projeto = atividadeEntity.getProjeto();
        User usuarioResponsavelProjeto = projeto.getUsuarioResponsavel();

        // Verifica se o usuário que está editando a atividade é o responsável pelo projeto
        if (!usuarioResponsavelProjeto.getId_usuarios().equals(userId)) {
            throw new RuntimeException("Apenas o responsável pelo projeto pode editar atividades.");
        }
        if (updateAtividadeDto.nomeAtividade() != null) {
            atividadeEntity.setNomeAtividade(updateAtividadeDto.nomeAtividade());
        }
        if (updateAtividadeDto.descricao() != null) {
            atividadeEntity.setDescricao_atividade(updateAtividadeDto.descricao());
        }
        if (updateAtividadeDto.dataInicio() != null) {
            atividadeEntity.setDataInicio(updateAtividadeDto.dataInicio());
        }
        if (updateAtividadeDto.dataFim() != null) {
            atividadeEntity.setDataFim(updateAtividadeDto.dataFim());
        }
        if (updateAtividadeDto.status() != null) {
            atividadeEntity.setStatus(updateAtividadeDto.status());
        }
    
        // Save variant using the existing entity with the current version
        atividadeRepository.save(atividadeEntity);
    }

    public void deleteById(String atividadeId, UUID userId) {
        Atividade atividadeEntity = atividadeRepository.findById(UUID.fromString(atividadeId))
            .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Projeto projeto = atividadeEntity.getProjeto();
        User usuarioResponsavelProjeto = projeto.getUsuarioResponsavel();

        // Verifica se o usuário que está deletando a atividade é o responsável pelo projeto
        if (!usuarioResponsavelProjeto.getId_usuarios().equals(userId)) {
            throw new RuntimeException("Apenas o responsável pelo projeto pode deletar atividades.");
        }

        atividadeRepository.deleteById(UUID.fromString(atividadeId));
    }
}
