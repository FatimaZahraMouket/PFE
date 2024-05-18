package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.repository.PlanningRepository;
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
 * Integration tests for the {@link PlanningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanningResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_JOUR_NUMERO = 1;
    private static final Integer UPDATED_JOUR_NUMERO = 2;

    private static final String ENTITY_API_URL = "/api/plannings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanningMockMvc;

    private Planning planning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createEntity(EntityManager em) {
        Planning planning = new Planning().titre(DEFAULT_TITRE).description(DEFAULT_DESCRIPTION).jourNumero(DEFAULT_JOUR_NUMERO);
        return planning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createUpdatedEntity(EntityManager em) {
        Planning planning = new Planning().titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).jourNumero(UPDATED_JOUR_NUMERO);
        return planning;
    }

    @BeforeEach
    public void initTest() {
        planning = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanning() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();
        // Create the Planning
        restPlanningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isCreated());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate + 1);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testPlanning.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlanning.getJourNumero()).isEqualTo(DEFAULT_JOUR_NUMERO);
    }

    @Test
    @Transactional
    void createPlanningWithExistingId() throws Exception {
        // Create the Planning with an existing ID
        planning.setId(1L);

        int databaseSizeBeforeCreate = planningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlannings() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get all the planningList
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jourNumero").value(hasItem(DEFAULT_JOUR_NUMERO)));
    }

    @Test
    @Transactional
    void getPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get the planning
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL_ID, planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planning.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.jourNumero").value(DEFAULT_JOUR_NUMERO));
    }

    @Test
    @Transactional
    void getNonExistingPlanning() throws Exception {
        // Get the planning
        restPlanningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning
        Planning updatedPlanning = planningRepository.findById(planning.getId()).get();
        // Disconnect from session so that the updates on updatedPlanning are not directly saved in db
        em.detach(updatedPlanning);
        updatedPlanning.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).jourNumero(UPDATED_JOUR_NUMERO);

        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlanning.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testPlanning.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlanning.getJourNumero()).isEqualTo(UPDATED_JOUR_NUMERO);
    }

    @Test
    @Transactional
    void putNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planning.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planning))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planning))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testPlanning.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlanning.getJourNumero()).isEqualTo(DEFAULT_JOUR_NUMERO);
    }

    @Test
    @Transactional
    void fullUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).jourNumero(UPDATED_JOUR_NUMERO);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testPlanning.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlanning.getJourNumero()).isEqualTo(UPDATED_JOUR_NUMERO);
    }

    @Test
    @Transactional
    void patchNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planning))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planning))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planning.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeDelete = planningRepository.findAll().size();

        // Delete the planning
        restPlanningMockMvc
            .perform(delete(ENTITY_API_URL_ID, planning.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
