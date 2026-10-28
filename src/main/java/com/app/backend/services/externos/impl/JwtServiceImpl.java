package com.app.backend.services.externos.impl;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.backend.services.externos.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
// Servicio central para emitir y validar JWT usados en autenticacion/autorizacion.
public class JwtServiceImpl implements IJwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey obtenerClaveInicioSesion() {
        // Convierte la clave configurada a la llave HMAC usada por jjwt.
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String extraerUsuario(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Integer extraerIdUsuario(String token) {
        return extractClaim(token, claims -> claims.get("idUsuario", Integer.class));
    }

    @Override
    public Integer extraerIdCarrera(String token) {
        return extractClaim(token, claims -> claims.get("idCarrera", Integer.class));
    }

    @Override
    public Integer extraerIdRol(String token) {
        return extractClaim(token, claims -> claims.get("idRol", Integer.class));
    }

    @Override
    public String extraerRol(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Todo acceso a claims pasa por este punto para mantener una unica ruta de parseo.
        final Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveInicioSesion())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    @Override
    public String generarToken(Map<String, Object> extraClaims,String username) {
        // El token incorpora claims de contexto (rol, carrera, facultad, etc.)
        // para reducir consultas extra durante la sesion.
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(obtenerClaveInicioSesion())
                .compact();
    }

    @Override
    public Boolean esTokenValido(String token, UserDetails userDetails) {
        final String username = extraerUsuario(token);
        return (username.equals(userDetails.getUsername())) && !esTokenExpirado(token);
    }

    private Boolean esTokenExpirado(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
