package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class RegraTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regra.class);
        Regra regra1 = new Regra();
        regra1.setId(1L);
        Regra regra2 = new Regra();
        regra2.setId(regra1.getId());
        assertThat(regra1).isEqualTo(regra2);
        regra2.setId(2L);
        assertThat(regra1).isNotEqualTo(regra2);
        regra1.setId(null);
        assertThat(regra1).isNotEqualTo(regra2);
    }
}
