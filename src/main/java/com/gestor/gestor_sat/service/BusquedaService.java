package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.BusquedaAvanzadaDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.ClienteMapper;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.spec.ClienteSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class BusquedaService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public Page<ClienteResponseDTO> buscarConFiltros(BusquedaAvanzadaDTO f, Pageable pageable) {
        // 5.7: validar que haya al menos 1 filtro
        if ((f.getNombre()==null || f.getNombre().isBlank()) &&
            (f.getDpi()==null || f.getDpi().isBlank()) &&
            f.getFechaInicio()==null && f.getFechaFin()==null &&
            (f.getEstado()==null || f.getEstado().isBlank())) {
            throw new CustomExceptions.FiltrosVaciosException("Debe proporcionar al menos un filtro");
        }

        Specification<Cliente> spec = Specification.where(ClienteSpecifications.nombreContains(f.getNombre()))
                .and(ClienteSpecifications.dpiEquals(f.getDpi()))
                .and(ClienteSpecifications.consultaEntre(f.getFechaInicio(), f.getFechaFin()))
                .and(ClienteSpecifications.consultaEstado(f.getEstado()));

        return clienteRepository.findAll(spec, pageable).map(clienteMapper::toResponseDTO);
    }
}
