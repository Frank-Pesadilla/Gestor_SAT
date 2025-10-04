package com.gestor.gestor_sat.dto; 
import java.util.Map;

import lombok.AllArgsConstructor; 
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor; 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class EstadisticasDTO { 
private Long totalClientes; 
private Long tramitesMesActual; 
private Map<String, Long> tramitesPorEstado; 
private Map<String, Long> topTiposTramites; 
private Map<String, Long> tramitesPorMes; 
private Map<String, Long> clientesNuevosPorMes; 
} 