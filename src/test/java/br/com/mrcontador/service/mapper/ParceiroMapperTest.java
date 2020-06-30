package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ParceiroMapperTest {

    private ParceiroMapper parceiroMapper;

    @BeforeEach
    public void setUp() {
        parceiroMapper = new ParceiroMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(parceiroMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(parceiroMapper.fromId(null)).isNull();
    }
}
