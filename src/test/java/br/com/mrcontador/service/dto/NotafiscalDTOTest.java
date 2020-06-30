package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class NotafiscalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotafiscalDTO.class);
        NotafiscalDTO notafiscalDTO1 = new NotafiscalDTO();
        notafiscalDTO1.setId(1L);
        NotafiscalDTO notafiscalDTO2 = new NotafiscalDTO();
        assertThat(notafiscalDTO1).isNotEqualTo(notafiscalDTO2);
        notafiscalDTO2.setId(notafiscalDTO1.getId());
        assertThat(notafiscalDTO1).isEqualTo(notafiscalDTO2);
        notafiscalDTO2.setId(2L);
        assertThat(notafiscalDTO1).isNotEqualTo(notafiscalDTO2);
        notafiscalDTO1.setId(null);
        assertThat(notafiscalDTO1).isNotEqualTo(notafiscalDTO2);
    }
}
