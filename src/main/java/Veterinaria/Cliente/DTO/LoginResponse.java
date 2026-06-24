package Veterinaria.Cliente.DTO; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
}
