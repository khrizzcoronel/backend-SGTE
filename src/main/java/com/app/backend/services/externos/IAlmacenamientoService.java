package com.app.backend.services.externos;

import org.springframework.web.multipart.MultipartFile;

public interface IAlmacenamientoService {
    String subirArchivos(MultipartFile file);
    byte[] descargarArchivos(String fileName);
    String obtenerContextoArchivo(String fileName);
    void eliminarArchivo(String fileName);
}
