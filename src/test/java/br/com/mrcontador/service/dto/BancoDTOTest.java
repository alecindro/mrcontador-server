package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class BancoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BancoDTO.class);
        BancoDTO bancoDTO1 = new BancoDTO();
        bancoDTO1.setId(1L);
        BancoDTO bancoDTO2 = new BancoDTO();
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO2.setId(bancoDTO1.getId());
        assertThat(bancoDTO1).isEqualTo(bancoDTO2);
        bancoDTO2.setId(2L);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO1.setId(null);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
    }
}
