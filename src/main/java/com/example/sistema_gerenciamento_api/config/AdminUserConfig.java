package com.example.sistema_gerenciamento_api.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.sistema_gerenciamento_api.entity.Role;
import com.example.sistema_gerenciamento_api.entity.User;
import com.example.sistema_gerenciamento_api.repository.RoleRepository;
import com.example.sistema_gerenciamento_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());   

        if (roleAdmin == null) {
            // Handle the case where the admin role is not found
            // You could create the role here, or throw an exception, etc.
            throw new RuntimeException("Admin role not found");
        }

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
            (user) -> {
                System.out.println("User admin already exists");
            }, 
            () -> {
                var user = new User();
                user.setNome("Admin");
                user.setEmail("admin@hotmail.com");
                user.setSenha(passwordEncoder.encode("1234"));
                user.setRoles(Set.of(roleAdmin));
                user.setData_criacao(Instant.now());
                user.setUltimoLogin(LocalDateTime.now());
                userRepository.save(user);
            }
        );
    }

}
