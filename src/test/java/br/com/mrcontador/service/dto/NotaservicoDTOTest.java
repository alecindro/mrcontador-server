package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class NotaservicoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaservicoDTO.class);
        NotaservicoDTO notaservicoDTO1 = new NotaservicoDTO();
        notaservicoDTO1.setId(1L);
        NotaservicoDTO notaservicoDTO2 = new NotaservicoDTO();
        assertThat(notaservicoDTO1).isNotEqualTo(notaservicoDTO2);
        notaservicoDTO2.setId(notaservicoDTO1.getId());
        assertThat(notaservicoDTO1).isEqualTo(notaservicoDTO2);
        notaservicoDTO2.setId(2L);
        assertThat(notaservicoDTO1).isNotEqualTo(notaservicoDTO2);
        notaservicoDTO1.setId(null);
        assertThat(notaservicoDTO1).isNotEqualTo(notaservicoDTO2);
    }
}
