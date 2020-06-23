package br.com.mrcontador.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mrcontador.web.rest.TestUtil;

public class ContadorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contador.class);
        Contador contador1 = new Contador();
        contador1.setId(1L);
        Contador contador2 = new Contador();
        contador2.setId(contador1.getId());
        assertThat(contador1).isEqualTo(contador2);
        contador2.setId(2L);
        assertThat(contador1).isNotEqualTo(contador2);
        contador1.setId(null);
        assertThat(contador1).isNotEqualTo(contador2);
    }
}
