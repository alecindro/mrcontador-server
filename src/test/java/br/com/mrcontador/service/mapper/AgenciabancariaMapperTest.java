package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AgenciabancariaMapperTest {

    private AgenciabancariaMapper agenciabancariaMapper;

    @BeforeEach
    public void setUp() {
        agenciabancariaMapper = new AgenciabancariaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(agenciabancariaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(agenciabancariaMapper.fromId(null)).isNull();
    }
}
