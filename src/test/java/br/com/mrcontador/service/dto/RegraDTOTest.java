package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class RegraDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegraDTO.class);
        RegraDTO regraDTO1 = new RegraDTO();
        regraDTO1.setId(1L);
        RegraDTO regraDTO2 = new RegraDTO();
        assertThat(regraDTO1).isNotEqualTo(regraDTO2);
        regraDTO2.setId(regraDTO1.getId());
        assertThat(regraDTO1).isEqualTo(regraDTO2);
        regraDTO2.setId(2L);
        assertThat(regraDTO1).isNotEqualTo(regraDTO2);
        regraDTO1.setId(null);
        assertThat(regraDTO1).isNotEqualTo(regraDTO2);
    }
}
