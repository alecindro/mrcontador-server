package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ComprovanteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comprovante.class);
        Comprovante comprovante1 = new Comprovante();
        comprovante1.setId(1L);
        Comprovante comprovante2 = new Comprovante();
        comprovante2.setId(comprovante1.getId());
        assertThat(comprovante1).isEqualTo(comprovante2);
        comprovante2.setId(2L);
        assertThat(comprovante1).isNotEqualTo(comprovante2);
        comprovante1.setId(null);
        assertThat(comprovante1).isNotEqualTo(comprovante2);
    }
}
