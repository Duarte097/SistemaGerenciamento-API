package com.example.sistema_gerenciamento_api.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/users").permitAll() //hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/users").permitAll() 
            .requestMatchers(HttpMethod.GET, "/users/{userId}").permitAll() 
            .requestMatchers(HttpMethod.PUT, "/users/{userId}").permitAll() 
            .requestMatchers(HttpMethod.DELETE, "/users/{userId}").permitAll() 
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/projetos").permitAll()
            .requestMatchers(HttpMethod.GET, "/projetos").permitAll()
            .requestMatchers(HttpMethod.GET, "/projetos/{projetoId}").permitAll() 
            .requestMatchers(HttpMethod.GET, "/projetos/{nomeProjeto}").permitAll()
            .requestMatchers(HttpMethod.PUT, "/projetos/{projetoId}").permitAll() 
            .requestMatchers(HttpMethod.DELETE, "/projetos/{projetoId}").permitAll() 
            .requestMatchers(HttpMethod.POST, "/atividades").permitAll()
            .requestMatchers(HttpMethod.GET, "/atividades").permitAll()
            .requestMatchers(HttpMethod.GET, "/atividades/{atividadeId}").permitAll() 
            .requestMatchers(HttpMethod.PUT, "/atividades/{atividadeId}").permitAll() 
            .requestMatchers(HttpMethod.DELETE, "/atividades/{atividadeId}").permitAll() 
            .requestMatchers(HttpMethod.POST, "/lancamentoHoras").permitAll()
            .requestMatchers(HttpMethod.GET, "/lancamentoHoras").permitAll()
            .requestMatchers(HttpMethod.GET, "/lancamentoHoras/{lancamentoHorasId}").permitAll() 
            .requestMatchers(HttpMethod.PUT, "/lancamentoHoras/{lancamentoHorasId}").permitAll() 
            .requestMatchers(HttpMethod.DELETE, "/lancamentoHoras/{lancamentoHorasId}").permitAll() 
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }


    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
