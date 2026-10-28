package com.app.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.backend.repositories.sistema.CredencialRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
// Configura los componentes base de autenticacion para Spring Security.
// Este archivo conecta credenciales en BD con el modelo UserDetails.
public class ConfigAplicacion {
    private final CredencialRepository credencialRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        // Traduce la credencial propia del dominio al UserDetails de Spring.
        return username -> credencialRepository.findByNombreUsuario(username)
                .map(credencial -> {
                    String rol = credencial.getUsuario().getRoles().stream()
                            .findFirst()
                            .map(r -> r.getNombreRol())
                            .orElse("USER");

                    return User.builder()
                            .username(credencial.getNombreUsuario())
                            .password(credencial.getHashContrasena())
                            .roles(rol)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado en la base de datos"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Proveedor basado en DAO que usa el UserDetailsService anterior.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());       
        return authProvider;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Nota: NoOpPasswordEncoder se mantiene por compatibilidad actual.
        // En produccion se recomienda BCryptPasswordEncoder.
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
