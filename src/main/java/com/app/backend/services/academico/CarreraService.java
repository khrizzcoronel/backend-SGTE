package com.app.backend.services.academico;

import com.app.backend.dtos.academico.response.CarreraDTO;
import lombok.NonNull;
import java.util.List;

public interface CarreraService {
    List<CarreraDTO> listarTodas();
    List<CarreraDTO> listarPorFacultad(@NonNull Integer idFacultad);
    CarreraDTO obtenerPorId(@NonNull Integer id);
    CarreraDTO crear(CarreraDTO dto);
    CarreraDTO actualizar(@NonNull Integer id, CarreraDTO dto);
    void eliminar(@NonNull Integer id);
}
