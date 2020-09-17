package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotaservicoMapperTest {

    private NotaservicoMapper notaservicoMapper;

    @BeforeEach
    public void setUp() {
        notaservicoMapper = new NotaservicoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(notaservicoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notaservicoMapper.fromId(null)).isNull();
    }
}
