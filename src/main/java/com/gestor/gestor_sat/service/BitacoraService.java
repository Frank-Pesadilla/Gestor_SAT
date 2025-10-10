package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.entity.Bitacora;
import com.gestor.gestor_sat.repository.BitacoraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j
public class BitacoraService {
    private final BitacoraRepository bitacoraRepository;

    @Transactional
    public void registrarAccion(String usuario, String accion, String detalles, String ipAddress) {
        Bitacora b = Bitacora.builder()
                .usuario(usuario)
                .accion(accion)
                .detalles(detalles)
                .ipAddress(ipAddress)
                .build();
        bitacoraRepository.save(b);
        log.debug("Bitácora registrada: {} - {}", usuario, accion);
    }
}
