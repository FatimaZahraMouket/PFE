package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.repository.PlanningRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Planning}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlanningResource {

    private final Logger log = LoggerFactory.getLogger(PlanningResource.class);

    private static final String ENTITY_NAME = "planning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanningRepository planningRepository;

    public PlanningResource(PlanningRepository planningRepository) {
        this.planningRepository = planningRepository;
    }

    /**
     * {@code POST  /plannings} : Create a new planning.
     *
     * @param planning the planning to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planning, or with status {@code 400 (Bad Request)} if the planning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plannings")
    public ResponseEntity<Planning> createPlanning(@RequestBody Planning planning) throws URISyntaxException {
        log.debug("REST request to save Planning : {}", planning);
        if (planning.getId() != null) {
            throw new BadRequestAlertException("A new planning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Planning result = planningRepository.save(planning);
        return ResponseEntity
            .created(new URI("/api/plannings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plannings/:id} : Updates an existing planning.
     *
     * @param id the id of the planning to save.
     * @param planning the planning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planning,
     * or with status {@code 400 (Bad Request)} if the planning is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plannings/{id}")
    public ResponseEntity<Planning> updatePlanning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Planning planning
    ) throws URISyntaxException {
        log.debug("REST request to update Planning : {}, {}", id, planning);
        if (planning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Planning result = planningRepository.save(planning);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planning.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plannings/:id} : Partial updates given fields of an existing planning, field will ignore if it is null
     *
     * @param id the id of the planning to save.
     * @param planning the planning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planning,
     * or with status {@code 400 (Bad Request)} if the planning is not valid,
     * or with status {@code 404 (Not Found)} if the planning is not found,
     * or with status {@code 500 (Internal Server Error)} if the planning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plannings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Planning> partialUpdatePlanning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Planning planning
    ) throws URISyntaxException {
        log.debug("REST request to partial update Planning partially : {}, {}", id, planning);
        if (planning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planning.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Planning> result = planningRepository
            .findById(planning.getId())
            .map(existingPlanning -> {
                if (planning.getTitre() != null) {
                    existingPlanning.setTitre(planning.getTitre());
                }
                if (planning.getDescription() != null) {
                    existingPlanning.setDescription(planning.getDescription());
                }
                if (planning.getJourNumero() != null) {
                    existingPlanning.setJourNumero(planning.getJourNumero());
                }

                return existingPlanning;
            })
            .map(planningRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planning.getId().toString())
        );
    }

    /**
     * {@code GET  /plannings} : get all the plannings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plannings in body.
     */
    @GetMapping("/plannings")
    public ResponseEntity<List<Planning>> getAllPlannings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Plannings");
        Page<Planning> page = planningRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plannings/:id} : get the "id" planning.
     *
     * @param id the id of the planning to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planning, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plannings/{id}")
    public ResponseEntity<Planning> getPlanning(@PathVariable Long id) {
        log.debug("REST request to get Planning : {}", id);
        Optional<Planning> planning = planningRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(planning);
    }

    /**
     * {@code DELETE  /plannings/:id} : delete the "id" planning.
     *
     * @param id the id of the planning to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plannings/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable Long id) {
        log.debug("REST request to delete Planning : {}", id);
        planningRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
