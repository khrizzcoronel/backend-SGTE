package com.app.backend.services.externos.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.backend.services.externos.IAlmacenamientoService;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlmacenamientoServiceImpl implements IAlmacenamientoService{

    private final BlobServiceClient blobServiceClient;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Override
    public String subirArchivos(MultipartFile file) {
        String originalFilename = file != null ? file.getOriginalFilename() : null;
        return subirArchivosAzure(file, originalFilename);
    }

    @Override
    public byte[] descargarArchivos(String fileName) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Archivo no encontrado: " + fileName);
        }
    }

    @Override
    public String obtenerContextoArchivo(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".svg"))  return "image/svg+xml";
        if (lower.endsWith(".pdf"))  return "application/pdf";
        return "application/octet-stream";
    }

    @Override
    public void eliminarArchivo(String fileName) {
        try {
            if (containerName == null || containerName.isEmpty()) {
                throw new IllegalStateException("El nombre del contenedor no está configurado.");
            }
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            if (blobClient.exists()) {
                blobClient.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el archivo: " + fileName, e);
        }
    }
    
    private String subirArchivosAzure(MultipartFile file, String targetFileName){
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            if (!containerClient.exists()) {
                containerClient.create();
            }

            String fallbackName = file.getOriginalFilename() != null
                    ? file.getOriginalFilename()
                    : "file.bin";
            String fileName = sanitizeBlobName(targetFileName, fallbackName);

            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return blobClient.getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage());
        }
    }

    private String sanitizeBlobName(String targetFileName, String fallbackName) {
        String raw = (targetFileName == null || targetFileName.isBlank()) ? fallbackName : targetFileName;
        String normalized = raw.replace('\\', '/').trim();

        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }

        normalized = normalized.replace("..", "");
        return normalized.isBlank() ? fallbackName : normalized;
    }
}
