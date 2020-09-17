package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExtratoMapperTest {

    private ExtratoMapper extratoMapper;

    @BeforeEach
    public void setUp() {
        extratoMapper = new ExtratoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(extratoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(extratoMapper.fromId(null)).isNull();
    }
}
