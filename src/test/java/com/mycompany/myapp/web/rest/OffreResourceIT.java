package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Offre;
import com.mycompany.myapp.domain.enumeration.Language;
import com.mycompany.myapp.repository.OffreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OffreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OffreResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/offres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OffreRepository offreRepository;

    @Mock
    private OffreRepository offreRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreMockMvc;

    private Offre offre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createEntity(EntityManager em) {
        Offre offre = new Offre()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .prix(DEFAULT_PRIX)
            .image(DEFAULT_IMAGE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .language(DEFAULT_LANGUAGE);
        return offre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createUpdatedEntity(EntityManager em) {
        Offre offre = new Offre()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .prix(UPDATED_PRIX)
            .image(UPDATED_IMAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .language(UPDATED_LANGUAGE);
        return offre;
    }

    @BeforeEach
    public void initTest() {
        offre = createEntity(em);
    }

    @Test
    @Transactional
    void createOffre() throws Exception {
        int databaseSizeBeforeCreate = offreRepository.findAll().size();
        // Create the Offre
        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offre)))
            .andExpect(status().isCreated());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeCreate + 1);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testOffre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOffre.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testOffre.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testOffre.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOffre.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testOffre.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void createOffreWithExistingId() throws Exception {
        // Create the Offre with an existing ID
        offre.setId(1L);

        int databaseSizeBeforeCreate = offreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offre)))
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffres() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offre.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOffresWithEagerRelationshipsIsEnabled() throws Exception {
        when(offreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOffreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(offreRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOffresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(offreRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOffreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(offreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get the offre
        restOffreMockMvc
            .perform(get(ENTITY_API_URL_ID, offre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offre.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOffre() throws Exception {
        // Get the offre
        restOffreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre
        Offre updatedOffre = offreRepository.findById(offre.getId()).get();
        // Disconnect from session so that the updates on updatedOffre are not directly saved in db
        em.detach(updatedOffre);
        updatedOffre
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .prix(UPDATED_PRIX)
            .image(UPDATED_IMAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .language(UPDATED_LANGUAGE);

        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testOffre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOffre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testOffre.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOffre.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOffre.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOffre.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre.prix(UPDATED_PRIX).image(UPDATED_IMAGE).language(UPDATED_LANGUAGE);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testOffre.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOffre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testOffre.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOffre.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOffre.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testOffre.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .prix(UPDATED_PRIX)
            .image(UPDATED_IMAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .language(UPDATED_LANGUAGE);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testOffre.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOffre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testOffre.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOffre.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOffre.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOffre.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(offre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeDelete = offreRepository.findAll().size();

        // Delete the offre
        restOffreMockMvc
            .perform(delete(ENTITY_API_URL_ID, offre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
