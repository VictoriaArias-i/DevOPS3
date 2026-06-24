package Veterinaria.Cliente.Exception; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String message) {
        super(message);
    }
}
