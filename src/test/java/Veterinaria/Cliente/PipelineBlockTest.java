package Veterinaria.Cliente;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

class PipelineBlockTest {

    @Test
    void testIntencionalmente_falla_para_bloquear_pipeline() {
        // TEST DE DEMOSTRACIÓN: simula un error crítico que bloquea el despliegue
        fail("ERROR CRÍTICO: Este fallo detiene el pipeline antes del despliegue a producción.");
    }
}
