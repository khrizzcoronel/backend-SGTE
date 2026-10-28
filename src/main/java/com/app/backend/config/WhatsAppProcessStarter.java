package com.app.backend.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WhatsAppProcessStarter {

    @Value("${whatsapp.service.url:http://localhost:7075}")
    private String whatsappServiceUrl;

    private Process whatsappProcess;

    @EventListener(ApplicationReadyEvent.class)
    public void iniciarServicioWhatsApp() {
        try {
            File directorio = buscarDirectorioWhatsApp();

            if (directorio == null) {
                log.warn("No se encontró la carpeta whatsapp-service. El servicio NO se iniciará automáticamente.");
                return;
            }

            URI serviceUri = URI.create(whatsappServiceUrl);
            String host = serviceUri.getHost();
            int puerto = serviceUri.getPort();
            if (host == null || puerto < 1 || puerto > 65535) {
                log.error("URL del servicio de WhatsApp inválida: {}", whatsappServiceUrl);
                return;
            }

            if (puertoOcupado(host, puerto)) {
                if (servicioWhatsAppDisponible()) {
                    log.info("Servicio de WhatsApp ya disponible en {}", whatsappServiceUrl);
                } else {
                    log.error("El puerto {} está ocupado por otro servicio; WhatsApp no se iniciará.", puerto);
                }
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("node", "server.js");
            pb.directory(directorio);
            pb.environment().put("WHATSAPP_PORT", String.valueOf(puerto));
            pb.redirectErrorStream(true);

            whatsappProcess = pb.start();
            log.info("Servicio de WhatsApp iniciado automáticamente desde: {}", directorio);

            Process proceso = whatsappProcess;
            Thread logThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(proceso.getInputStream()))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        log.info("[WhatsApp-Service] {}", linea);
                    }
                } catch (Exception e) {
                    log.debug("Terminó la lectura de logs de WhatsApp: {}", e.getMessage());
                }
            });
            logThread.setDaemon(true);
            logThread.setName("whatsapp-log-reader");
            logThread.start();

        } catch (Exception e) {
            log.error("Error al iniciar el servicio de WhatsApp: {}", e.getMessage());
        }
    }

    private boolean puertoOcupado(String host, int puerto) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, puerto), 500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean servicioWhatsAppDisponible() {
        try {
            HttpURLConnection conexion = (HttpURLConnection) URI.create(
                    whatsappServiceUrl + "/api/whatsapp/estado").toURL().openConnection();
            conexion.setConnectTimeout(1000);
            conexion.setReadTimeout(1000);
            try {
                if (conexion.getResponseCode() != 200) {
                    return false;
                }
                try (var respuesta = conexion.getInputStream()) {
                    return new String(respuesta.readAllBytes(), StandardCharsets.UTF_8)
                            .contains("SGTE WhatsApp Service");
                }
            } finally {
                conexion.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
    }

    private File buscarDirectorioWhatsApp() {
        File cwd = new File(System.getProperty("user.dir"));
        for (int i = 0; i < 3; i++) {
            File candidato = new File(cwd, "whatsapp-service");
            if (new File(candidato, "server.js").exists()) {
                return candidato;
            }
            cwd = cwd.getParentFile();
            if (cwd == null) break;
        }
        return null;
    }

    @PreDestroy
    public void detenerServicioWhatsApp() {
        if (whatsappProcess != null && whatsappProcess.isAlive()) {
            whatsappProcess.destroy();
            log.info("Servicio de WhatsApp detenido.");
        }
    }
}
