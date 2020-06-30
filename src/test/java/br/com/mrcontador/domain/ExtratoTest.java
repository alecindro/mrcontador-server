package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ExtratoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extrato.class);
        Extrato extrato1 = new Extrato();
        extrato1.setId(1L);
        Extrato extrato2 = new Extrato();
        extrato2.setId(extrato1.getId());
        assertThat(extrato1).isEqualTo(extrato2);
        extrato2.setId(2L);
        assertThat(extrato1).isNotEqualTo(extrato2);
        extrato1.setId(null);
        assertThat(extrato1).isNotEqualTo(extrato2);
    }
}
