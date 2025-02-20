package com.example.sistema_gerenciamento_api.dto.userDto;

import java.time.LocalDateTime;

public record CreateUserDto(String nome, String email, String senha, String perfil, String role, LocalDateTime data_criacao) {

} 
