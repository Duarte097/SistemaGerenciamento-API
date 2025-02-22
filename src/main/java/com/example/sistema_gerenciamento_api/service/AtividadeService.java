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
    

    public UUID createAtividade(AtividadeDTO atividadeDto,UUID id,  UUID userId, UUID projetoId) {
        //Projeto projeto = getProjetoById(projetoId);
        //User usuarioResponsavel = getUserById(userId);

        /*Optional<Atividade> atividadeExistente = atividadeRepository.findById(id);
        if (!atividadeExistente.isPresent()) {
            throw new EntityNotFoundException("Atividade não encontrada com o ID: " + id);
        }*/


        //System.out.println("ID do usuário recebido: " + userId);
        //System.out.println("ID do projeto recebido: " + projetoId);

        var entity = new Atividade(
            null, 
            null, 
            atividadeDto.nomeAtividade(),             
            atividadeDto.descricao(), 
            atividadeDto.dataInicio(), 
            atividadeDto.dataFim(),
            atividadeDto.status(), 
            null,
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
        return projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    public Optional<Atividade> getAtividadeById(String atividadeId) {
       return atividadeRepository.findById(UUID.fromString(atividadeId));
    }

    public List<Atividade> listAtividades(){
        return atividadeRepository.findAll();
    }

    public void updateAtividadeDto(String atividadeId, AtividadeDTO updateAtividadeDto){
        var atividadeExists = atividadeRepository.findById(UUID.fromString(atividadeId));    

        if(atividadeExists.isPresent()){
            var atividadeEntity = atividadeExists.get();

            if(updateAtividadeDto.nomeAtividade() != null){
                atividadeEntity.setNomeAtividade(updateAtividadeDto.nomeAtividade());
            }
            if(updateAtividadeDto.descricao() != null){
                atividadeEntity.setDescricao_atividade(updateAtividadeDto.descricao());
            }
            if(updateAtividadeDto.dataInicio()!= null){
                atividadeEntity.setDataInicio(updateAtividadeDto.dataInicio());
            }
            if(updateAtividadeDto.dataFim()!= null){
                atividadeEntity.setDataFim(updateAtividadeDto.dataFim());
            }
            if(updateAtividadeDto.status()!= null){
                atividadeEntity.setStatus(updateAtividadeDto.status());
            }
            /*if(updateAtividadeDto.idUsuario()!= null){
                atividadeEntity.setUser(updateAtividadeDto.idUsuario());
            }*/
            atividadeRepository.save(atividadeEntity);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void deleteById(String atividadeId){
        var atividadeExists = atividadeRepository.existsById(UUID.fromString(atividadeId));

        if(atividadeExists){
            userRepository.deleteById(UUID.fromString(atividadeId));
        }else{
            throw new RuntimeException("User not found");
        }
    }
}
