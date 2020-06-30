package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ExtratoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtratoDTO.class);
        ExtratoDTO extratoDTO1 = new ExtratoDTO();
        extratoDTO1.setId(1L);
        ExtratoDTO extratoDTO2 = new ExtratoDTO();
        assertThat(extratoDTO1).isNotEqualTo(extratoDTO2);
        extratoDTO2.setId(extratoDTO1.getId());
        assertThat(extratoDTO1).isEqualTo(extratoDTO2);
        extratoDTO2.setId(2L);
        assertThat(extratoDTO1).isNotEqualTo(extratoDTO2);
        extratoDTO1.setId(null);
        assertThat(extratoDTO1).isNotEqualTo(extratoDTO2);
    }
}
