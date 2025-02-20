package com.example.sistema_gerenciamento_api.controllers;

import com.example.sistema_gerenciamento_api.dto.userDto.CreateUserDto;
import com.example.sistema_gerenciamento_api.dto.userDto.UpdateUserDto;
import com.example.sistema_gerenciamento_api.entity.Role;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.RoleRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleRepository roleRepository, 
                          BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto userDto) {
        if (userDto.senha() == null || userDto.senha().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha não pode ser vazia.");
        }

        String roleName = (userDto.role() != null && !userDto.role().isEmpty()) ? userDto.role() : Role.Values.USUARIO.name();
        
        // Obtém a Role do banco
        var role = roleRepository.findByName(roleName);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role inválida.");
        }

        // Cria o usuário
        var userId = userService.createUser(userDto);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar usuário.");
        }

        // Instancia e salva o usuário
        var user = new User();
        user.setId_usuarios(userId);
        user.setNome(userDto.nome());
        user.setEmail(userDto.email());
        user.setSenha(passwordEncoder.encode(userDto.senha()));
        user.setPerfil(userDto.perfil());
        user.setData_criacao(userDto.data_criacao());
        user.setRoles(Set.of(role));
        userRepository.save(user);

        System.out.println("Data de criação: " + user.getData_criacao());
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso! ID: " + userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateById(@PathVariable String userId,
                                           @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUserDto(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable String userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
