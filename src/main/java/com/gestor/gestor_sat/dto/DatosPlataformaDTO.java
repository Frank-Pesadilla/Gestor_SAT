package com.gestor.gestor_sat.dto; 
 
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; 
import lombok.NoArgsConstructor; 
 
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class DatosPlataformaDTO { 
    private Long id; 
@NotBlank(message = "El usuario de SAT no puede estar vacío") 
private String usuarioSat; 
@NotBlank(message = "La contraseña de SAT no puede estar vacía") 
private String contrasenaSat; 
private String usuarioIgss; 
private String contrasenaIgss; 
private String usuarioRenap; 
private String contrasenaRenap; 
private String usuarioMinfin; 
private String contrasenaMinfin; 
private String observaciones; 
}