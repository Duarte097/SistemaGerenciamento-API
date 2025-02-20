package com.example.sistema_gerenciamento_api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.sistema_gerenciamento_api.dto.userDto.CreateUserDto;
import com.example.sistema_gerenciamento_api.dto.userDto.UpdateUserDto;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UUID createUser(CreateUserDto userDto) {

       var entity = new User(
        null, 
        userDto.nome(), 
        userDto.email(), 
        userDto.senha(), 
        userDto.perfil(),
        userDto.data_criacao(),
        null);

        var userSaved = userRepository.save(entity);
        return userSaved.getId_usuarios();
    }

    public Optional<User> getUserById(String userId) {
       return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public void updateUserDto(String userId, UpdateUserDto updateUserDto){
        var userExists = userRepository.findById(UUID.fromString(userId));    

        if(userExists.isPresent()){
            var userEntity = userExists.get();

            if(updateUserDto.nome() != null){
                userEntity.setNome(updateUserDto.nome());
            }
            if(updateUserDto.senha() != null){
                userEntity.setSenha(updateUserDto.senha());
            }
            userRepository.save(userEntity);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void deleteById(String userId){
        var userExists = userRepository.existsById(UUID.fromString(userId));

        if(userExists){
            userRepository.deleteById(UUID.fromString(userId));
        }else{
            throw new RuntimeException("User not found");
        }
    }
}
