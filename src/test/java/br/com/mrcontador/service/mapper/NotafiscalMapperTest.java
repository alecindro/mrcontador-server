package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotafiscalMapperTest {

    private NotafiscalMapper notafiscalMapper;

    @BeforeEach
    public void setUp() {
        notafiscalMapper = new NotafiscalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(notafiscalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notafiscalMapper.fromId(null)).isNull();
    }
}
