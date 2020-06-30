package br.com.mrcontador.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BancoMapperTest {

    private BancoMapper bancoMapper;

    @BeforeEach
    public void setUp() {
        bancoMapper = new BancoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bancoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bancoMapper.fromId(null)).isNull();
    }
}
