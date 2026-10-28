package com.app.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
// Punto de entrada del backend. EnableAsync permite ejecutar procesos no bloqueantes,
// por ejemplo notificaciones por correo/WhatsApp sin detener el request principal.
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
