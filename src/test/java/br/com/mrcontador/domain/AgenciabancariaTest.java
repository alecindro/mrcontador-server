package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class AgenciabancariaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agenciabancaria.class);
        Agenciabancaria agenciabancaria1 = new Agenciabancaria();
        agenciabancaria1.setId(1L);
        Agenciabancaria agenciabancaria2 = new Agenciabancaria();
        agenciabancaria2.setId(agenciabancaria1.getId());
        assertThat(agenciabancaria1).isEqualTo(agenciabancaria2);
        agenciabancaria2.setId(2L);
        assertThat(agenciabancaria1).isNotEqualTo(agenciabancaria2);
        agenciabancaria1.setId(null);
        assertThat(agenciabancaria1).isNotEqualTo(agenciabancaria2);
    }
}
