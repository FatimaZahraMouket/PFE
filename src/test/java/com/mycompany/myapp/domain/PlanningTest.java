package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planning.class);
        Planning planning1 = new Planning();
        planning1.setId(1L);
        Planning planning2 = new Planning();
        planning2.setId(planning1.getId());
        assertThat(planning1).isEqualTo(planning2);
        planning2.setId(2L);
        assertThat(planning1).isNotEqualTo(planning2);
        planning1.setId(null);
        assertThat(planning1).isNotEqualTo(planning2);
    }
}
