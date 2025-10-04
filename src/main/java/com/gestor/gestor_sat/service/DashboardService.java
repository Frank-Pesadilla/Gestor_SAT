package com.gestor.gestor_sat.service; 
import com.gestor.gestor_sat.dto.EstadisticasDTO; 
import com.gestor.gestor_sat.model.enums.TramiteEstado; 
import com.gestor.gestor_sat.repository.ClienteRepository; 
import com.gestor.gestor_sat.repository.ConsultaTramiteRepository; 
import lombok.RequiredArgsConstructor; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
import java.time.LocalDate; 
import java.time.YearMonth; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
 
@Service 
@RequiredArgsConstructor 
public class DashboardService { 
     
    private final ClienteRepository clienteRepository; 
    private final ConsultaTramiteRepository consultaTramiteRepository; 
     
    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticas() {
        
        Long totalClientes = clienteRepository.count();
        
        LocalDate inicioMes = YearMonth.now().atDay(1);
        LocalDate finMes = YearMonth.now().atEndOfMonth();
        Long tramitesMesActual = consultaTramiteRepository
                .countByFechaTramiteBetween(inicioMes, finMes); // CAMBIO aquí
        
        Map<String, Long> tramitesPorEstado = new HashMap<>();
        for (TramiteEstado estado : TramiteEstado.values()) {
            Long count = consultaTramiteRepository.countByEstado(estado);
            tramitesPorEstado.put(estado.name(), count);
        }
        
        List<Object[]> topTramites = consultaTramiteRepository
                .findTop5TiposTramitesMasSolicitados();
        
        Map<String, Long> topTiposTramites = new HashMap<>();
        for (Object[] result : topTramites) {
            String tipoTramite = (String) result[0];
            Long cantidad = (Long) result[1];
            topTiposTramites.put(tipoTramite, cantidad);
        }
        
        Map<String, Long> tramitesPorMes = obtenerTramitesPorMes();
        Map<String, Long> clientesNuevosPorMes = obtenerClientesNuevosPorMes();
        
        return EstadisticasDTO.builder()
                .totalClientes(totalClientes)
                .tramitesMesActual(tramitesMesActual)
                .tramitesPorEstado(tramitesPorEstado)
                .topTiposTramites(topTiposTramites)
                .tramitesPorMes(tramitesPorMes)
                .clientesNuevosPorMes(clientesNuevosPorMes)
                .build();
    }

    private Map<String, Long> obtenerTramitesPorMes() {
        Map<String, Long> tramitesPorMes = new HashMap<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth mes = YearMonth.now().minusMonths(i);
            LocalDate inicio = mes.atDay(1);
            LocalDate fin = mes.atEndOfMonth();
            Long count = consultaTramiteRepository.countByFechaTramiteBetween(inicio, fin); // CAMBIO aquí
            tramitesPorMes.put(mes.toString(), count);
        }
        return tramitesPorMes;
    } 
     
    private Map<String, Long> obtenerClientesNuevosPorMes() { 
        Map<String, Long> clientesPorMes = new HashMap<>(); 
        for (int i = 5; i >= 0; i--) { 
            YearMonth mes = YearMonth.now().minusMonths(i); 
            LocalDate inicio = mes.atDay(1); 
            LocalDate fin = mes.atEndOfMonth(); 
            Long count = clienteRepository.countByCreatedAtBetween( 
                inicio.atStartOfDay(), fin.atTime(23, 59, 59)); 
            clientesPorMes.put(mes.toString(), count); 
        } 
        return clientesPorMes; 
    } 
} 
