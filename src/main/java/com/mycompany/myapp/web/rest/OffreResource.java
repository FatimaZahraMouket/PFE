package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Offre;
import com.mycompany.myapp.repository.OffreRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Offre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OffreResource {

    private final Logger log = LoggerFactory.getLogger(OffreResource.class);

    private static final String ENTITY_NAME = "offre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OffreRepository offreRepository;

    public OffreResource(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    /**
     * {@code POST  /offres} : Create a new offre.
     *
     * @param offre the offre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offre, or with status {@code 400 (Bad Request)} if the offre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offres")
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) throws URISyntaxException {
        log.debug("REST request to save Offre : {}", offre);
        if (offre.getId() != null) {
            throw new BadRequestAlertException("A new offre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Offre result = offreRepository.save(offre);
        return ResponseEntity
            .created(new URI("/api/offres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offres/:id} : Updates an existing offre.
     *
     * @param id the id of the offre to save.
     * @param offre the offre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offre,
     * or with status {@code 400 (Bad Request)} if the offre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offres/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable(value = "id", required = false) final Long id, @RequestBody Offre offre)
        throws URISyntaxException {
        log.debug("REST request to update Offre : {}, {}", id, offre);
        if (offre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Offre result = offreRepository.save(offre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /offres/:id} : Partial updates given fields of an existing offre, field will ignore if it is null
     *
     * @param id the id of the offre to save.
     * @param offre the offre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offre,
     * or with status {@code 400 (Bad Request)} if the offre is not valid,
     * or with status {@code 404 (Not Found)} if the offre is not found,
     * or with status {@code 500 (Internal Server Error)} if the offre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/offres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Offre> partialUpdateOffre(@PathVariable(value = "id", required = false) final Long id, @RequestBody Offre offre)
        throws URISyntaxException {
        log.debug("REST request to partial update Offre partially : {}, {}", id, offre);
        if (offre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Offre> result = offreRepository
            .findById(offre.getId())
            .map(existingOffre -> {
                if (offre.getLabel() != null) {
                    existingOffre.setLabel(offre.getLabel());
                }
                if (offre.getDescription() != null) {
                    existingOffre.setDescription(offre.getDescription());
                }
                if (offre.getPrix() != null) {
                    existingOffre.setPrix(offre.getPrix());
                }
                if (offre.getImage() != null) {
                    existingOffre.setImage(offre.getImage());
                }
                if (offre.getLatitude() != null) {
                    existingOffre.setLatitude(offre.getLatitude());
                }
                if (offre.getLongitude() != null) {
                    existingOffre.setLongitude(offre.getLongitude());
                }
                if (offre.getLanguage() != null) {
                    existingOffre.setLanguage(offre.getLanguage());
                }

                return existingOffre;
            })
            .map(offreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offre.getId().toString())
        );
    }

    /**
     * {@code GET  /offres} : get all the offres.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offres in body.
     */
    @GetMapping("/offres")
    public ResponseEntity<List<Offre>> getAllOffres(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Offres");
        Page<Offre> page;
        if (eagerload) {
            page = offreRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = offreRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offres/:id} : get the "id" offre.
     *
     * @param id the id of the offre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offres/{id}")
    public ResponseEntity<Offre> getOffre(@PathVariable Long id) {
        log.debug("REST request to get Offre : {}", id);
        Optional<Offre> offre = offreRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(offre);
    }

    /**
     * {@code DELETE  /offres/:id} : delete the "id" offre.
     *
     * @param id the id of the offre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offres/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) {
        log.debug("REST request to delete Offre : {}", id);
        offreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
