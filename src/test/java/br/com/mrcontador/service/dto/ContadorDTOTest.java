package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ContadorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContadorDTO.class);
        ContadorDTO contadorDTO1 = new ContadorDTO();
        contadorDTO1.setId(1L);
        ContadorDTO contadorDTO2 = new ContadorDTO();
        assertThat(contadorDTO1).isNotEqualTo(contadorDTO2);
        contadorDTO2.setId(contadorDTO1.getId());
        assertThat(contadorDTO1).isEqualTo(contadorDTO2);
        contadorDTO2.setId(2L);
        assertThat(contadorDTO1).isNotEqualTo(contadorDTO2);
        contadorDTO1.setId(null);
        assertThat(contadorDTO1).isNotEqualTo(contadorDTO2);
    }
}
