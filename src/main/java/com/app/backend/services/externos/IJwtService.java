package com.app.backend.services.externos;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface IJwtService {
    String extraerUsuario(String token);
    Integer extraerIdUsuario(String token);
    Integer extraerIdCarrera(String token);
    Integer extraerIdRol(String token);
    String extraerRol(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generarToken(Map<String, Object> extraClaims,String userName);
    Boolean esTokenValido(String token, UserDetails userDetails);
}
