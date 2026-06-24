package Veterinaria.Cliente.Controller; // NOSONAR: nombre de paquete heredado, renombrar rompería toda la estructura del proyecto

import Veterinaria.Cliente.DTO.LoginRequest;
import Veterinaria.Cliente.DTO.LoginResponse;
import Veterinaria.Cliente.Service.AuthService;
import Veterinaria.Cliente.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@SuppressWarnings("java:S5122") // CORS abierto intencionalmente — API pública de login
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    // POST /auth/login - Genera un token JWT si las credenciales son válidas
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        log.info("Solicitud de login recibida para usuario: {}", request.getUsername()); // NOSONAR
        if (!authService.validarCredenciales(request.getUsername(), request.getPassword())) {
            log.warn("Login fallido para usuario: {}", request.getUsername()); // NOSONAR
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales incorrectas"));
        }

        String token = jwtService.generateToken(request.getUsername());
        log.info("Token generado exitosamente para usuario: {}", request.getUsername()); // NOSONAR
        return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
    }
}
