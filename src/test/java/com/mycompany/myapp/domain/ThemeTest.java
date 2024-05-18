package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theme.class);
        Theme theme1 = new Theme();
        theme1.setId(1L);
        Theme theme2 = new Theme();
        theme2.setId(theme1.getId());
        assertThat(theme1).isEqualTo(theme2);
        theme2.setId(2L);
        assertThat(theme1).isNotEqualTo(theme2);
        theme1.setId(null);
        assertThat(theme1).isNotEqualTo(theme2);
    }
}
