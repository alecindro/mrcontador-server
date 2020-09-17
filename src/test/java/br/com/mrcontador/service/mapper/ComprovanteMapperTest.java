package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ComprovanteMapperTest {

    private ComprovanteMapper comprovanteMapper;

    @BeforeEach
    public void setUp() {
        comprovanteMapper = new ComprovanteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(comprovanteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(comprovanteMapper.fromId(null)).isNull();
    }
}
