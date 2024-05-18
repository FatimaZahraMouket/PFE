package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pays;
import com.mycompany.myapp.repository.PaysRepository;
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
 * Integration tests for the {@link PaysResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaysResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_REVIEWS = 1;
    private static final Integer UPDATED_REVIEWS = 2;

    private static final String DEFAULT_CONTINENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTINENT = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Boolean DEFAULT_IS_SAVED = false;
    private static final Boolean UPDATED_IS_SAVED = true;

    private static final Boolean DEFAULT_IS_LIKED = false;
    private static final Boolean UPDATED_IS_LIKED = true;

    private static final String ENTITY_API_URL = "/api/pays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaysRepository paysRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaysMockMvc;

    private Pays pays;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .reviews(DEFAULT_REVIEWS)
            .continent(DEFAULT_CONTINENT)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .isSaved(DEFAULT_IS_SAVED)
            .isLiked(DEFAULT_IS_LIKED);
        return pays;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createUpdatedEntity(EntityManager em) {
        Pays pays = new Pays()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .reviews(UPDATED_REVIEWS)
            .continent(UPDATED_CONTINENT)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .isSaved(UPDATED_IS_SAVED)
            .isLiked(UPDATED_IS_LIKED);
        return pays;
    }

    @BeforeEach
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();
        // Create the Pays
        restPaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testPays.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPays.getReviews()).isEqualTo(DEFAULT_REVIEWS);
        assertThat(testPays.getContinent()).isEqualTo(DEFAULT_CONTINENT);
        assertThat(testPays.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPays.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testPays.getIsSaved()).isEqualTo(DEFAULT_IS_SAVED);
        assertThat(testPays.getIsLiked()).isEqualTo(DEFAULT_IS_LIKED);
    }

    @Test
    @Transactional
    void createPaysWithExistingId() throws Exception {
        // Create the Pays with an existing ID
        pays.setId(1L);

        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList
        restPaysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reviews").value(hasItem(DEFAULT_REVIEWS)))
            .andExpect(jsonPath("$.[*].continent").value(hasItem(DEFAULT_CONTINENT)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].isSaved").value(hasItem(DEFAULT_IS_SAVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isLiked").value(hasItem(DEFAULT_IS_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc
            .perform(get(ENTITY_API_URL_ID, pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.reviews").value(DEFAULT_REVIEWS))
            .andExpect(jsonPath("$.continent").value(DEFAULT_CONTINENT))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.isSaved").value(DEFAULT_IS_SAVED.booleanValue()))
            .andExpect(jsonPath("$.isLiked").value(DEFAULT_IS_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays
        Pays updatedPays = paysRepository.findById(pays.getId()).get();
        // Disconnect from session so that the updates on updatedPays are not directly saved in db
        em.detach(updatedPays);
        updatedPays
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .reviews(UPDATED_REVIEWS)
            .continent(UPDATED_CONTINENT)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .isSaved(UPDATED_IS_SAVED)
            .isLiked(UPDATED_IS_LIKED);

        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPays.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPays))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPays.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPays.getReviews()).isEqualTo(UPDATED_REVIEWS);
        assertThat(testPays.getContinent()).isEqualTo(UPDATED_CONTINENT);
        assertThat(testPays.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPays.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPays.getIsSaved()).isEqualTo(UPDATED_IS_SAVED);
        assertThat(testPays.getIsLiked()).isEqualTo(UPDATED_IS_LIKED);
    }

    @Test
    @Transactional
    void putNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pays.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pays))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pays))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaysWithPatch() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays using partial update
        Pays partialUpdatedPays = new Pays();
        partialUpdatedPays.setId(pays.getId());

        partialUpdatedPays.label(UPDATED_LABEL).longitude(UPDATED_LONGITUDE).isSaved(UPDATED_IS_SAVED).isLiked(UPDATED_IS_LIKED);

        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPays.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPays.getReviews()).isEqualTo(DEFAULT_REVIEWS);
        assertThat(testPays.getContinent()).isEqualTo(DEFAULT_CONTINENT);
        assertThat(testPays.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPays.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPays.getIsSaved()).isEqualTo(UPDATED_IS_SAVED);
        assertThat(testPays.getIsLiked()).isEqualTo(UPDATED_IS_LIKED);
    }

    @Test
    @Transactional
    void fullUpdatePaysWithPatch() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays using partial update
        Pays partialUpdatedPays = new Pays();
        partialUpdatedPays.setId(pays.getId());

        partialUpdatedPays
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .reviews(UPDATED_REVIEWS)
            .continent(UPDATED_CONTINENT)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .isSaved(UPDATED_IS_SAVED)
            .isLiked(UPDATED_IS_LIKED);

        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPays.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPays.getReviews()).isEqualTo(UPDATED_REVIEWS);
        assertThat(testPays.getContinent()).isEqualTo(UPDATED_CONTINENT);
        assertThat(testPays.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPays.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPays.getIsSaved()).isEqualTo(UPDATED_IS_SAVED);
        assertThat(testPays.getIsLiked()).isEqualTo(UPDATED_IS_LIKED);
    }

    @Test
    @Transactional
    void patchNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pays.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pays))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pays))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        pays.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeDelete = paysRepository.findAll().size();

        // Delete the pays
        restPaysMockMvc
            .perform(delete(ENTITY_API_URL_ID, pays.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
