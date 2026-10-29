package com.daw.crudapi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.daw.crudapi.model.LogAccion;
import com.daw.crudapi.repository.LogAccionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogAccionRepository logRepository;

    public void registrar(String accion, String entidad, Long idEntidad, String detalle) {
        logRepository.save(new LogAccion(null, LocalDateTime.now(), accion, entidad, idEntidad, detalle));
    }
}
