package Veterinaria.Cliente; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClienteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClienteApplication.class, args);
    }
}
