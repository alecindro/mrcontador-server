package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ParceiroDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParceiroDTO.class);
        ParceiroDTO parceiroDTO1 = new ParceiroDTO();
        parceiroDTO1.setId(1L);
        ParceiroDTO parceiroDTO2 = new ParceiroDTO();
        assertThat(parceiroDTO1).isNotEqualTo(parceiroDTO2);
        parceiroDTO2.setId(parceiroDTO1.getId());
        assertThat(parceiroDTO1).isEqualTo(parceiroDTO2);
        parceiroDTO2.setId(2L);
        assertThat(parceiroDTO1).isNotEqualTo(parceiroDTO2);
        parceiroDTO1.setId(null);
        assertThat(parceiroDTO1).isNotEqualTo(parceiroDTO2);
    }
}
