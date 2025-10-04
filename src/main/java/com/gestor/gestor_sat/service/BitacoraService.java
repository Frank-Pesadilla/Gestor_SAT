package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.BitacoraDTO;
import com.gestor.gestor_sat.dto.BitacoraFiltroDTO;
import com.gestor.gestor_sat.model.Bitacora;
import com.gestor.gestor_sat.model.Usuario;
import com.gestor.gestor_sat.repository.BitacoraRepository;
import com.gestor.gestor_sat.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BitacoraService {
    
    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;
    
    /**
     * Registra una acción en la bitácora
     */
    @Transactional
    public void registrarAccion(String accion, String detalles, Long idUsuario, 
                                String ipAddress, String userAgent) {
        try {
            Usuario usuario = null;
            if (idUsuario != null) {
                usuario = usuarioRepository.findById(idUsuario).orElse(null);
            }
            
            Bitacora bitacora = Bitacora.builder()
                    .usuario(usuario)
                    .accion(accion)
                    .detalles(detalles)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();
            
            bitacoraRepository.save(bitacora);
            log.info("Acción registrada en bitácora: {} por usuario: {}", accion, idUsuario);
            
        } catch (Exception e) {
            log.error("Error al registrar en bitácora", e);
        }
    }
    
    /**
     * Consulta la bitácora con filtros
     */
    public Page<BitacoraDTO> consultarBitacora(BitacoraFiltroDTO filtros, Pageable pageable) {
        log.info("Consultando bitácora con filtros");
        
        Usuario usuario = null;
        if (filtros.getIdUsuario() != null) {
            usuario = usuarioRepository.findById(filtros.getIdUsuario()).orElse(null);
        }
        
        Page<Bitacora> bitacoras = bitacoraRepository.buscarConFiltros(
                usuario,
                filtros.getAccion(),
                filtros.getFechaInicio(),
                filtros.getFechaFin(),
                pageable
        );
        
        return bitacoras.map(this::mapearADTO);
    }
    
    private BitacoraDTO mapearADTO(Bitacora bitacora) {
        return BitacoraDTO.builder()
                .id(bitacora.getId())
                .nombreUsuario(bitacora.getUsuario() != null ? 
                              bitacora.getUsuario().getNombre() : "Sistema")
                .accion(bitacora.getAccion())
                .detalles(bitacora.getDetalles())
                .ipAddress(bitacora.getIpAddress())
                .fecha(bitacora.getCreatedAt())
                .build();
    }
}
