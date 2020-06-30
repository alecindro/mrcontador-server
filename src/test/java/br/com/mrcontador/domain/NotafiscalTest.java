package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class NotafiscalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notafiscal.class);
        Notafiscal notafiscal1 = new Notafiscal();
        notafiscal1.setId(1L);
        Notafiscal notafiscal2 = new Notafiscal();
        notafiscal2.setId(notafiscal1.getId());
        assertThat(notafiscal1).isEqualTo(notafiscal2);
        notafiscal2.setId(2L);
        assertThat(notafiscal1).isNotEqualTo(notafiscal2);
        notafiscal1.setId(null);
        assertThat(notafiscal1).isNotEqualTo(notafiscal2);
    }
}
