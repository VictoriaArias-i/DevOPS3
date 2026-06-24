package Veterinaria.Cliente.Service; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    // Valida credenciales hardcodeadas para demostración
    // En producción esto se consultaría en base de datos con contraseña hasheada
    public Boolean validarCredenciales(String username, String password) {
        log.info("Intentando autenticar usuario: {}", username); // NOSONAR
        boolean valido = "admin".equals(username) && "1234".equals(password);
        if (valido) {
            log.info("Autenticación exitosa para usuario: {}", username); // NOSONAR
        } else {
            log.warn("Credenciales incorrectas para usuario: {}", username); // NOSONAR
        }
        return valido;
    }
}
