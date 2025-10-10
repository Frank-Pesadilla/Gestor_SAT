package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.ClienteCreateDTO;
import com.gestor.gestor_sat.dto.ClienteResponseDTO;
import com.gestor.gestor_sat.dto.ClienteDetalleDTO;
import com.gestor.gestor_sat.entity.Cliente;
import com.gestor.gestor_sat.entity.Usuario;
import com.gestor.gestor_sat.exception.CustomExceptions;
import com.gestor.gestor_sat.mapper.ClienteMapper;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import com.gestor.gestor_sat.repository.DatosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;
    private final DatosRepository datosRepository;

    // 1.2 obtenerClientePorId
    @Transactional(readOnly = true)
    public ClienteDetalleDTO obtenerClientePorId(Long id) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.ClienteNoEncontradoException(id));
        return mapToDetalle(cliente);
    }

    // 1.3 buscarClientePorDpi
    @Transactional(readOnly = true)
    public ClienteDetalleDTO buscarClientePorDpi(String dpi) {
        var cliente = clienteRepository.findByDpi(dpi)
                .orElseThrow(() -> new CustomExceptions.ClienteNoEncontradoException(dpi));
        return mapToDetalle(cliente);
    }

    // 1.4 listarTodosLosClientes (paginado)
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarTodosLosClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(clienteMapper::toResponseDTO);
    }

    /**
     * CU-SAT001: Registrar Cliente
     */
    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteCreateDTO dto) {
        log.info("Iniciando registro de cliente con DPI: {}", dto.getDpi());

        // Validar DPI único
        if (clienteRepository.existsByDpi(dto.getDpi())) {
            log.error("El DPI {} ya está registrado", dto.getDpi());
            throw new CustomExceptions.DpiYaExisteException(dto.getDpi());
        }

        // Validar mayoría de edad
        validarEdadMinima(dto.getFechaNacimiento());

        // Mapear y relacionar usuario (si aplica)
        Cliente cliente = clienteMapper.toEntity(dto);

        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new CustomExceptions.UsuarioNoEncontradoException(dto.getIdUsuario()));

            if (clienteRepository.existsByUsuarioId(dto.getIdUsuario())) {
                log.error("El usuario {} ya tiene un cliente asociado", dto.getIdUsuario());
                throw new CustomExceptions.UsuarioYaTieneClienteException(dto.getIdUsuario());
            }
            cliente.setUsuario(usuario);
        }

        // Guardar
        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente registrado exitosamente con ID: {}", clienteGuardado.getIdCliente());
        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    // ---- helper para armar ClienteDetalleDTO con relaciones ----
    private ClienteDetalleDTO mapToDetalle(Cliente c) {
        var detalle = ClienteDetalleDTO.builder()
                .idCliente(c.getIdCliente())
                .nombreCompleto(c.getNombreCompleto())
                .dpi(c.getDpi())
                .fechaNacimiento(c.getFechaNacimiento())
                .telefono(c.getTelefono())
                .direccion(c.getDireccion())
                .idUsuario(c.getUsuario() != null ? c.getUsuario().getIdUsuario() : null)
                .emailUsuario(c.getUsuario() != null ? c.getUsuario().getEmail() : null)
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();

        // Datos (tabla datos)
        datosRepository.findByClienteIdCliente(c.getIdCliente()).ifPresent(d -> {
            detalle.setNit(d.getNit());
            detalle.setNis(d.getNis());
            detalle.setEmailAlterno(d.getEmail());
            detalle.setCuentaBancaria(d.getCuentaBancaria());
        });

        // Archivos
        if (c.getArchivos() != null) {
            var archivos = c.getArchivos().stream().map(a ->
                    ClienteDetalleDTO.ArchivoResumen.builder()
                            .idArchivos(a.getIdArchivos())
                            .nombreArchivo(a.getNombreArchivo())
                            .url(a.getRuta())
                            .fechaSubida(a.getFechaSubida())
                            .build()
            ).toList();
            detalle.setArchivos(archivos);
        }

        // Trámites (ConsultaTramite)
        if (c.getConsultaTramites() != null) {
            var tramites = c.getConsultaTramites().stream().map(ct ->
                    ClienteDetalleDTO.TramiteResumen.builder()
                            .idConsultaTramite(ct.getIdConsultaTramite())
                            .idTramites(ct.getTramite() != null ? ct.getTramite().getIdTramites() : null)
                            .nombreTramite(ct.getTramite() != null ? ct.getTramite().getNombre() : null)
                            .fechaTramite(ct.getFechaTramite())
                            .estado(ct.getEstado().name())
                            .build()
            ).toList();
            detalle.setTramites(tramites);
        }
        return detalle;
    }

    
    private void validarEdadMinima(LocalDate fechaNacimiento) {
        LocalDate fechaActual = LocalDate.now();
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();
        if (edad < 18) {
            log.error("Edad insuficiente: {} años", edad);
            throw new CustomExceptions.EdadMinimaException();
        }
        log.debug("Validación de edad exitosa: {} años", edad);
    }
}
