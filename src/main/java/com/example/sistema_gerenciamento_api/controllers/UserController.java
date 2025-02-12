package com.example.sistema_gerenciamento_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_gerenciamento_api.dto.userDto.CreateUserDto;
import com.example.sistema_gerenciamento_api.dto.userDto.UpdateUserDto;
import com.example.sistema_gerenciamento_api.entity.Role;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.RoleRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;
import com.example.sistema_gerenciamento_api.service.UserService;

import jakarta.transaction.Transactional;
import lombok.experimental.var;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/createusers")
public class UserController {
    
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto userDto){
        @SuppressWarnings("deprecation")
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        @SuppressWarnings("deprecation")
        var userId = userService.createUser(userDto);

        @SuppressWarnings("deprecation")
        var user = new User();
        user.setId_usuarios(userId);
        user.setNome(userDto.nome());
        user.setEmail(userDto.email());
        user.setSenha(passwordEncoder.encode(userDto.senha()));
        user.setRoles(Set.of(basicRole));
        userRepository.save(user);
    
        //return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
        @SuppressWarnings("deprecation")
        var user = userService.getUserById(userId);

        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        @SuppressWarnings("deprecation")
        var user = userService.listUsers();

        return ResponseEntity.ok(user);
    }

    @PutMapping("{userId}")
    public ResponseEntity<Void> updateById(@PathVariable("userId") String userId,
                                           @RequestBody UpdateUserDto updateUserDto){
        userService.updateUserDto(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
