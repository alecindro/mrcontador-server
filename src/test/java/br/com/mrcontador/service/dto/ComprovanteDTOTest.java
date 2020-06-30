package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ComprovanteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComprovanteDTO.class);
        ComprovanteDTO comprovanteDTO1 = new ComprovanteDTO();
        comprovanteDTO1.setId(1L);
        ComprovanteDTO comprovanteDTO2 = new ComprovanteDTO();
        assertThat(comprovanteDTO1).isNotEqualTo(comprovanteDTO2);
        comprovanteDTO2.setId(comprovanteDTO1.getId());
        assertThat(comprovanteDTO1).isEqualTo(comprovanteDTO2);
        comprovanteDTO2.setId(2L);
        assertThat(comprovanteDTO1).isNotEqualTo(comprovanteDTO2);
        comprovanteDTO1.setId(null);
        assertThat(comprovanteDTO1).isNotEqualTo(comprovanteDTO2);
    }
}
