package com.gestor.gestor_sat.service;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.exception.ClienteNoEncontradoException;
import com.gestor.gestor_sat.mapper.DatosMapper;
import com.gestor.gestor_sat.model.Cliente;
import com.gestor.gestor_sat.model.Datos;
import com.gestor.gestor_sat.repository.ClienteRepository;
import com.gestor.gestor_sat.repository.DatosRepository;
import com.gestor.gestor_sat.service.security.AESEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatosService {
    
    private final DatosRepository datosRepository;
    private final ClienteRepository clienteRepository;
    private final AESEncryptionService aesEncryptionService;
    private final DatosMapper datosMapper;
    
    @Transactional
    public DatosPlataformaDTO guardarContrasenas(Long idCliente, DatosPlataformaDTO dto) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ClienteNoEncontradoException(
                    "Cliente con ID " + idCliente + " no encontrado"));
        
        Datos datos = datosRepository.findByClienteId(idCliente)
                .orElse(Datos.builder().cliente(cliente).build());
        
        datos.setCliente(cliente);
        datos.setNit(dto.getNit());
        datos.setNis(dto.getNis());
        datos.setEmail(dto.getEmail());
        datos.setDpi(dto.getDpi());
        datos.setCuentaBancaria(dto.getCuentaBancaria());
        
        // Encriptar contraseñas si se proporcionan
        if (dto.getContrasenaAgenciaVirtual() != null) {
            datos.setContrasenaAgenciaVirtual(aesEncryptionService.encrypt(dto.getContrasenaAgenciaVirtual()));
        }
        if (dto.getContrasenaCorreo() != null) {
            datos.setContrasenaCorreo(aesEncryptionService.encrypt(dto.getContrasenaCorreo()));
        }
        if (dto.getContrasenaCgc() != null) {
            datos.setContrasenaCgc(aesEncryptionService.encrypt(dto.getContrasenaCgc()));
        }
        if (dto.getContrasenaConsultaGeneral() != null) {
            datos.setContrasenaConsultaGeneral(aesEncryptionService.encrypt(dto.getContrasenaConsultaGeneral()));
        }
        if (dto.getContrasenaRegahe() != null) {
            datos.setContrasenaRegahe(aesEncryptionService.encrypt(dto.getContrasenaRegahe()));
        }
        
        Datos guardado = datosRepository.save(datos);
        return datosMapper.toDTO(guardado, false);
    }
    
    @Transactional(readOnly = true)
    public DatosPlataformaDTO obtenerContrasenas(Long idCliente) {
        Datos datos = datosRepository.findByClienteId(idCliente)
                .orElseThrow(() -> new RuntimeException(
                    "No se encontraron datos de plataforma para el cliente"));
        
        return datosMapper.toDTO(datos, true);
    }
}