package com.example.sistema_gerenciamento_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public LacamentoHorasService(LancamentoHorasRepository lancamentoHorasRepository, AtividadeRepository atividadeRepository, UserRepository userRepository) {
        this.lancamentoHorasRepository = lancamentoHorasRepository;
        this.atividadeRepository = atividadeRepository;
        this.userRepository = userRepository;
    }

    public UUID createLancamentoHoras(LancamentoHorasDTO lancamentoHorasDTO, UUID userId, UUID atividadeId) {
        User usuarioResponsavel = getUserById(userId);
        Atividade atividade = getAtividadeById(atividadeId);

        if (!atividade.getStatus().equals("EM_ANDAMENTO") && !atividade.getStatus().equals("ABERTA")){
            throw new RuntimeException("Não é possível criar atividades para projetos com status diferente de EM_ANDAMENTO.");
        }

        // Verifica se o usuário que está lançando as horas é o responsável pela atividade
        if (!atividade.getUser().getId_usuarios().equals(userId)) {
            throw new RuntimeException("Apenas o responsável pela atividade pode lançar horas.");
        }

        var entity = new LancamentoHoras(
            null, 
            atividade, 
            usuarioResponsavel,
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


    public List<LancamentoHoras> listLancamentoHoras(UUID userId) {
        User usuario = getUserById(userId);
        if (usuario.getPerfil().equals("ADMIN")) {
            return lancamentoHorasRepository.findAll();
        } else {
            return lancamentoHorasRepository.findAll().stream()
                .filter(lancamentoHoras -> lancamentoHoras.getUser().getId_usuarios().equals(userId))
                .collect(Collectors.toList());
        }
    }

    public void updateLancamentoHorasDto(String lancamentoHorasId, LancamentoHorasDTO updateLancamentoHorasDto, UUID userId) {
        var lancamentoHorasExists = lancamentoHorasRepository.findById(UUID.fromString(lancamentoHorasId));

        if (lancamentoHorasExists.isPresent()) {
            var lancamentoHorasEntity = lancamentoHorasExists.get();
            Atividade atividade = lancamentoHorasEntity.getAtividade();

            // Verifica se o usuário que está editando o lançamento de horas é o responsável pela atividade
            if (!atividade.getUser().getId_usuarios().equals(userId)) {
                throw new RuntimeException("Apenas o responsável pela atividade pode editar lançamentos de horas.");
            }
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

    public void deleteById(String lancamentoHorasId, UUID userId) {
        var lancamentoHorasExists = lancamentoHorasRepository.findById(UUID.fromString(lancamentoHorasId));

        if (lancamentoHorasExists.isPresent()) {
            Atividade atividade = lancamentoHorasExists.get().getAtividade();

            // Verifica se o usuário que está deletando o lançamento de horas é o responsável pela atividade
            if (!atividade.getUser().getId_usuarios().equals(userId)) {
                throw new RuntimeException("Apenas o responsável pela atividade pode deletar lançamentos de horas.");
            }

            lancamentoHorasRepository.deleteById(UUID.fromString(lancamentoHorasId));
        } else {
            throw new RuntimeException("Lançamento de horas não encontrado");
        }
    }
}
