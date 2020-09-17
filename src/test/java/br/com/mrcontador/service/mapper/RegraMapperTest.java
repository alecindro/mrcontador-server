package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegraMapperTest {

    private RegraMapper regraMapper;

    @BeforeEach
    public void setUp() {
        regraMapper = new RegraMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(regraMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(regraMapper.fromId(null)).isNull();
    }
}
