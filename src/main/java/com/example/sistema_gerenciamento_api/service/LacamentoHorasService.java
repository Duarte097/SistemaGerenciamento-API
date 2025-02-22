package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.sistema_gerenciamento_api.dto.lancamentoHorasDTO.LancamentoHorasDTO;
import com.example.sistema_gerenciamento_api.entity.Atividade;
import com.example.sistema_gerenciamento_api.entity.LancamentoHoras;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.AtividadeRepository;
import com.example.sistema_gerenciamento_api.repository.LancamentoHorasRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;

@Service
public class LacamentoHorasService {
    private LancamentoHorasRepository lancamentoHorasRepository;
    private UserRepository userRepository;
    private AtividadeRepository atividadeRepository;
    //private User user;

    public LacamentoHorasService(LancamentoHorasRepository lancamentoHorasRepository, AtividadeRepository atividadeRepository, UserRepository userRepository) {
        this.lancamentoHorasRepository = lancamentoHorasRepository;
        this.atividadeRepository = atividadeRepository;
        this.userRepository = userRepository;
    }

    public UUID createLancamentoHoras(LancamentoHorasDTO lancamentoHorasDTO, UUID userId, UUID atividadeId) {
        User usuarioResponsavel = getUserById(userId);
        //Atividade atividadeResponsavel = getAtividadeById(atividadeId);
        
        // Verifica o perfil do usuário
        if (!usuarioResponsavel.getPerfil().equals("ADMIN")) {
            throw new RuntimeException("Apenas usuários com perfil ADMIN podem criar projetos");
        }

        var entity = new LancamentoHoras(
            null, 
            null, 
            null,
            lancamentoHorasDTO.descricao(),
            lancamentoHorasDTO.dataInicio(),
            lancamentoHorasDTO.dataFim(),
            LocalDateTime.now()
        );

 
        var lancamentoHorasSaved = lancamentoHorasRepository.save(entity);
        return lancamentoHorasSaved.getId_lancamentos_horas();
    }

    public Optional<LancamentoHoras> getLancamentoHorasById(String lancamentoHorasId) {
        return lancamentoHorasRepository.findById(UUID.fromString(lancamentoHorasId));
     }

    public Atividade getAtividadeById(UUID atividadeId) {
       return atividadeRepository.findById(atividadeId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // Método para obter o usuário por ID
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    public List<LancamentoHoras> listLancamentoHoras(){
        return lancamentoHorasRepository.findAll();
    }

    public void updateLancamentoHorasDto(String lancamentoHorasId, LancamentoHorasDTO updateLancamentoHorasDto){
        var lancamentoHorasExists = lancamentoHorasRepository.findById(UUID.fromString(lancamentoHorasId));    

        if(lancamentoHorasExists.isPresent()){
            var lancamentoHorasEntity = lancamentoHorasExists.get();

            if(updateLancamentoHorasDto.descricao() != null){
                lancamentoHorasEntity.setDescricao(updateLancamentoHorasDto.descricao());
            }
            if(updateLancamentoHorasDto.dataInicio() != null){
                lancamentoHorasEntity.setDataInicio(updateLancamentoHorasDto.dataInicio());
            }
            if(updateLancamentoHorasDto.dataFim() != null){
                lancamentoHorasEntity.setDataFim(updateLancamentoHorasDto.dataFim());
            }
            lancamentoHorasRepository.save(lancamentoHorasEntity);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void deleteById(String lancamentoHorasId){
        var lancamentoHorasExists = lancamentoHorasRepository.existsById(UUID.fromString(lancamentoHorasId));

        if(lancamentoHorasExists){
            lancamentoHorasRepository.deleteById(UUID.fromString(lancamentoHorasId));
        }else{
            throw new RuntimeException("User not found");
        }
    }
}
