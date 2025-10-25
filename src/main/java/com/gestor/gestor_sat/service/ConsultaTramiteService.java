package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ConsultaTramiteCreateDTO;
import com.gestor.gestor_sat.dto.ConsultaTramiteHistorialDTO;
import com.gestor.gestor_sat.dto.ConsultaTramiteResponseDTO;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.ConsultaTramite;
import com.gestor.gestor_sat.entity.Tramite;
import com.gestor.gestor_sat.entity.enums.TramiteEstado;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.ConsultaTramiteRepository;
import com.gestor.gestor_sat.repository.TramiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaTramiteService {

    private final ConsultaTramiteRepository consultaRepo;
    private final ClienteRepository clienteRepo;
    private final TramiteRepository tramiteRepo;

    @Transactional
    public ConsultaTramiteResponseDTO crear(ConsultaTramiteCreateDTO dto) {
        Cliente cliente = clienteRepo.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Tramite tramite = tramiteRepo.findById(dto.getIdTramites())
                .orElseThrow(() -> new IllegalArgumentException("Trámite no encontrado"));

        TramiteEstado estado = dto.getEstado() != null ? dto.getEstado() : TramiteEstado.INICIADO;

        ConsultaTramite entidad = ConsultaTramite.builder()
                .cliente(cliente)
                .tramite(tramite)
                .fechaTramite(dto.getFechaTramite() != null ? dto.getFechaTramite() : LocalDate.now())
                .estado(estado)
                .createdBy(dto.getCreatedBy())
                .build();

        entidad = consultaRepo.save(entidad);
        return toResponse(entidad);
    }

    @Transactional(readOnly = true)
    public List<ConsultaTramiteHistorialDTO> historialPorCliente(Long idCliente) {
        return consultaRepo.findByClienteOrderByFechaDesc(idCliente)
                .stream()
                .map(this::toHistorial)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ConsultaTramiteHistorialDTO> historialPorClientePaged(Long idCliente, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConsultaTramite> pageData = consultaRepo.findByClienteIdClienteWithPagination(idCliente, pageable);
        return pageData.map(this::toHistorial);
    }

    private ConsultaTramiteHistorialDTO toHistorial(ConsultaTramite ct) {
        return ConsultaTramiteHistorialDTO.builder()
                .idConsultaTramite(ct.getIdConsultaTramite())
                .fechaTramite(ct.getFechaTramite())
                .estado(ct.getEstado())
                .estadoDescripcion(describeEstado(ct.getEstado()))
                .createdAt(ct.getCreatedAt())
                .updatedAt(ct.getUpdatedAt())
                .build();
    }

    private ConsultaTramiteResponseDTO toResponse(ConsultaTramite ct) {
        ConsultaTramiteResponseDTO r = new ConsultaTramiteResponseDTO();
        r.setIdConsultaTramite(ct.getIdConsultaTramite());
        r.setIdCliente(ct.getCliente().getIdCliente());
        r.setIdTramites(ct.getTramite().getIdTramites());
        r.setFechaTramite(ct.getFechaTramite());
        r.setEstado(ct.getEstado());
        return r;
    }

    // Si tu enum ya trae descripción, usa eso; si no, este helper es suficiente para la demo.
    private String describeEstado(TramiteEstado e) {
        if (e == null) return "";
        return switch (e) {
            case INICIADO -> "Trámite recién iniciado";
            case PENDIENTE -> "Trámite en proceso de revisión";
            case TERMINADO -> "Trámite completado";
        };
    }
}
