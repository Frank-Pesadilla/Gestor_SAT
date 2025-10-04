package com.gestor.gestor_sat.controller; 
 
<<<<<<< HEAD
<<<<<<< HEAD
import com.gestor.gestor_sat.dto.DatosPlataformaDTO; 
import com.gestor.gestor_sat.service.DatosService; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
=======
=======
>>>>>>> Mafer
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;

import com.gestor.gestor_sat.dto.DatosPlataformaDTO;
import com.gestor.gestor_sat.service.DatosService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
>>>>>>> Mafer
=======
>>>>>>> Mafer
 
@RestController 
@RequestMapping("/api/datos") 
@RequiredArgsConstructor 
public class DatosController { 
     
    private final DatosService datosService; 
     
    @PostMapping("/{idCliente}/contrasenas") 
    public ResponseEntity<DatosPlataformaDTO> guardarContrasenas( 
            @PathVariable Long idCliente, 
            @Valid @RequestBody DatosPlataformaDTO datosDTO) { 
        DatosPlataformaDTO resultado = datosService.guardarContrasenas(idCliente, datosDTO); 
        return ResponseEntity.ok(resultado); 
    } 
     
    @GetMapping("/{idCliente}/contrasenas") 
    public ResponseEntity<DatosPlataformaDTO> obtenerContrasenas(@PathVariable Long idCliente) { 
        DatosPlataformaDTO datos = datosService.obtenerContrasenas(idCliente); 
        return ResponseEntity.ok(datos); 
    } 
<<<<<<< HEAD
<<<<<<< HEAD
} 
=======
}
>>>>>>> Mafer
=======
}
>>>>>>> Mafer
