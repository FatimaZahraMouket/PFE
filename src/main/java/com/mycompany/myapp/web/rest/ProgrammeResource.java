package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Programme;
import com.mycompany.myapp.repository.ProgrammeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Programme}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProgrammeResource {

    private final Logger log = LoggerFactory.getLogger(ProgrammeResource.class);

    private static final String ENTITY_NAME = "programme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgrammeRepository programmeRepository;

    public ProgrammeResource(ProgrammeRepository programmeRepository) {
        this.programmeRepository = programmeRepository;
    }

    /**
     * {@code POST  /programmes} : Create a new programme.
     *
     * @param programme the programme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programme, or with status {@code 400 (Bad Request)} if the programme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programmes")
    public ResponseEntity<Programme> createProgramme(@RequestBody Programme programme) throws URISyntaxException {
        log.debug("REST request to save Programme : {}", programme);
        if (programme.getId() != null) {
            throw new BadRequestAlertException("A new programme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Programme result = programmeRepository.save(programme);
        return ResponseEntity
            .created(new URI("/api/programmes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /programmes/:id} : Updates an existing programme.
     *
     * @param id the id of the programme to save.
     * @param programme the programme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programme,
     * or with status {@code 400 (Bad Request)} if the programme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programmes/{id}")
    public ResponseEntity<Programme> updateProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Programme programme
    ) throws URISyntaxException {
        log.debug("REST request to update Programme : {}, {}", id, programme);
        if (programme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Programme result = programmeRepository.save(programme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /programmes/:id} : Partial updates given fields of an existing programme, field will ignore if it is null
     *
     * @param id the id of the programme to save.
     * @param programme the programme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programme,
     * or with status {@code 400 (Bad Request)} if the programme is not valid,
     * or with status {@code 404 (Not Found)} if the programme is not found,
     * or with status {@code 500 (Internal Server Error)} if the programme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/programmes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Programme> partialUpdateProgramme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Programme programme
    ) throws URISyntaxException {
        log.debug("REST request to partial update Programme partially : {}, {}", id, programme);
        if (programme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Programme> result = programmeRepository
            .findById(programme.getId())
            .map(existingProgramme -> {
                if (programme.getHeure() != null) {
                    existingProgramme.setHeure(programme.getHeure());
                }
                if (programme.getDescription() != null) {
                    existingProgramme.setDescription(programme.getDescription());
                }

                return existingProgramme;
            })
            .map(programmeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programme.getId().toString())
        );
    }

    /**
     * {@code GET  /programmes} : get all the programmes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programmes in body.
     */
    @GetMapping("/programmes")
    public ResponseEntity<List<Programme>> getAllProgrammes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Programmes");
        Page<Programme> page = programmeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programmes/:id} : get the "id" programme.
     *
     * @param id the id of the programme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programmes/{id}")
    public ResponseEntity<Programme> getProgramme(@PathVariable Long id) {
        log.debug("REST request to get Programme : {}", id);
        Optional<Programme> programme = programmeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(programme);
    }

    /**
     * {@code DELETE  /programmes/:id} : delete the "id" programme.
     *
     * @param id the id of the programme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programmes/{id}")
    public ResponseEntity<Void> deleteProgramme(@PathVariable Long id) {
        log.debug("REST request to delete Programme : {}", id);
        programmeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
