package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContadorMapperTest {

    private ContadorMapper contadorMapper;

    @BeforeEach
    public void setUp() {
        contadorMapper = new ContadorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contadorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contadorMapper.fromId(null)).isNull();
    }
}
