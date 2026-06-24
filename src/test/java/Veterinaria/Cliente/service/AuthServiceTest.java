package Veterinaria.Cliente.service;

import Veterinaria.Cliente.Service.AuthService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void validarCredenciales_debeRetornarTrue_cuandoCredencialesCorrectas() {
        Boolean resultado = authService.validarCredenciales("admin", "1234");
        assertThat(resultado).isTrue();
    }

    @Test
    void validarCredenciales_debeRetornarFalse_cuandoPasswordIncorrecta() {
        Boolean resultado = authService.validarCredenciales("admin", "wrongpass");
        assertThat(resultado).isFalse();
    }

    @Test
    void validarCredenciales_debeRetornarFalse_cuandoUsernameIncorrecto() {
        Boolean resultado = authService.validarCredenciales("otrouser", "1234");
        assertThat(resultado).isFalse();
    }

    @Test
    void validarCredenciales_debeRetornarFalse_cuandoAmbosIncorrectos() {
        Boolean resultado = authService.validarCredenciales("hacker", "password");
        assertThat(resultado).isFalse();
    }
}
