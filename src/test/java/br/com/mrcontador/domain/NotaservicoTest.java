package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class NotaservicoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notaservico.class);
        Notaservico notaservico1 = new Notaservico();
        notaservico1.setId(1L);
        Notaservico notaservico2 = new Notaservico();
        notaservico2.setId(notaservico1.getId());
        assertThat(notaservico1).isEqualTo(notaservico2);
        notaservico2.setId(2L);
        assertThat(notaservico1).isNotEqualTo(notaservico2);
        notaservico1.setId(null);
        assertThat(notaservico1).isNotEqualTo(notaservico2);
    }
}
