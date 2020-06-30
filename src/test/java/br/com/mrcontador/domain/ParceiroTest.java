package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ParceiroTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parceiro.class);
        Parceiro parceiro1 = new Parceiro();
        parceiro1.setId(1L);
        Parceiro parceiro2 = new Parceiro();
        parceiro2.setId(parceiro1.getId());
        assertThat(parceiro1).isEqualTo(parceiro2);
        parceiro2.setId(2L);
        assertThat(parceiro1).isNotEqualTo(parceiro2);
        parceiro1.setId(null);
        assertThat(parceiro1).isNotEqualTo(parceiro2);
    }
}
