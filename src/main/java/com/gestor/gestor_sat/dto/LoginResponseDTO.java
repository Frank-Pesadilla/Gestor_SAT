package com.gestor.gestor_sat.dto;
import lombok.*;
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
  private String username;
  private String message;
  private String token; // simulado por ahora
}
