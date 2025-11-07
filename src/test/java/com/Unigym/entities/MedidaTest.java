package com.Unigym.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedidaTest {

    @Test
    @DisplayName("IMC recalculado quando peso e altura são definidos")
    void imcCalculado() {
        Medida m = new Medida();
        m.setAltura(1.80); // altura primeiro
        m.setPeso(81.0);   // depois peso
        assertThat(m.getImc()).isNotNull();
        double esperado = 81.0 / (1.80 * 1.80);
        assertThat(m.getImc()).isEqualTo(esperado);

        // Atualiza peso e verifica novo IMC
        m.setPeso(90.0);
        double esperado2 = 90.0 / (1.80 * 1.80);
        assertThat(m.getImc()).isEqualTo(esperado2);
    }

    @Test
    @DisplayName("IMC permanece null se altura não definida ou zero")
    void imcNullQuandoAlturaInvalida() {
        Medida m = new Medida();
        m.setPeso(70.0);
        assertThat(m.getImc()).isNull(); // altura não definida
        m.setAltura(0.0);
        assertThat(m.getImc()).isNull(); // altura inválida
    }
}
