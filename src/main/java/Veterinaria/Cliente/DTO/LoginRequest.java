package Veterinaria.Cliente.DTO; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
