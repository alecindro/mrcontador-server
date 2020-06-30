package br.com.mrcontador.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class AgenciabancariaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgenciabancariaDTO.class);
        AgenciabancariaDTO agenciabancariaDTO1 = new AgenciabancariaDTO();
        agenciabancariaDTO1.setId(1L);
        AgenciabancariaDTO agenciabancariaDTO2 = new AgenciabancariaDTO();
        assertThat(agenciabancariaDTO1).isNotEqualTo(agenciabancariaDTO2);
        agenciabancariaDTO2.setId(agenciabancariaDTO1.getId());
        assertThat(agenciabancariaDTO1).isEqualTo(agenciabancariaDTO2);
        agenciabancariaDTO2.setId(2L);
        assertThat(agenciabancariaDTO1).isNotEqualTo(agenciabancariaDTO2);
        agenciabancariaDTO1.setId(null);
        assertThat(agenciabancariaDTO1).isNotEqualTo(agenciabancariaDTO2);
    }
}
