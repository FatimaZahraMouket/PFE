package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Programme;
import com.mycompany.myapp.repository.ProgrammeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProgrammeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgrammeResourceIT {

    private static final String DEFAULT_HEURE = "AAAAAAAAAA";
    private static final String UPDATED_HEURE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/programmes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgrammeMockMvc;

    private Programme programme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createEntity(EntityManager em) {
        Programme programme = new Programme().heure(DEFAULT_HEURE).description(DEFAULT_DESCRIPTION);
        return programme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programme createUpdatedEntity(EntityManager em) {
        Programme programme = new Programme().heure(UPDATED_HEURE).description(UPDATED_DESCRIPTION);
        return programme;
    }

    @BeforeEach
    public void initTest() {
        programme = createEntity(em);
    }

    @Test
    @Transactional
    void createProgramme() throws Exception {
        int databaseSizeBeforeCreate = programmeRepository.findAll().size();
        // Create the Programme
        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isCreated());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getHeure()).isEqualTo(DEFAULT_HEURE);
        assertThat(testProgramme.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProgrammeWithExistingId() throws Exception {
        // Create the Programme with an existing ID
        programme.setId(1L);

        int databaseSizeBeforeCreate = programmeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProgrammes() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get all the programmeList
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
            .andExpect(jsonPath("$.[*].heure").value(hasItem(DEFAULT_HEURE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        // Get the programme
        restProgrammeMockMvc
            .perform(get(ENTITY_API_URL_ID, programme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programme.getId().intValue()))
            .andExpect(jsonPath("$.heure").value(DEFAULT_HEURE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProgramme() throws Exception {
        // Get the programme
        restProgrammeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme
        Programme updatedProgramme = programmeRepository.findById(programme.getId()).get();
        // Disconnect from session so that the updates on updatedProgramme are not directly saved in db
        em.detach(updatedProgramme);
        updatedProgramme.heure(UPDATED_HEURE).description(UPDATED_DESCRIPTION);

        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgramme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getHeure()).isEqualTo(UPDATED_HEURE);
        assertThat(testProgramme.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(programme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme.heure(UPDATED_HEURE).description(UPDATED_DESCRIPTION);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getHeure()).isEqualTo(UPDATED_HEURE);
        assertThat(testProgramme.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProgrammeWithPatch() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

        // Update the programme using partial update
        Programme partialUpdatedProgramme = new Programme();
        partialUpdatedProgramme.setId(programme.getId());

        partialUpdatedProgramme.heure(UPDATED_HEURE).description(UPDATED_DESCRIPTION);

        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgramme))
            )
            .andExpect(status().isOk());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
        Programme testProgramme = programmeList.get(programmeList.size() - 1);
        assertThat(testProgramme.getHeure()).isEqualTo(UPDATED_HEURE);
        assertThat(testProgramme.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgramme() throws Exception {
        int databaseSizeBeforeUpdate = programmeRepository.findAll().size();
        programme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(programme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programme in the database
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgramme() throws Exception {
        // Initialize the database
        programmeRepository.saveAndFlush(programme);

        int databaseSizeBeforeDelete = programmeRepository.findAll().size();

        // Delete the programme
        restProgrammeMockMvc
            .perform(delete(ENTITY_API_URL_ID, programme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Programme> programmeList = programmeRepository.findAll();
        assertThat(programmeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
