package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Theme;
import com.mycompany.myapp.repository.ThemeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Theme}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ThemeResource {

    private final Logger log = LoggerFactory.getLogger(ThemeResource.class);

    private static final String ENTITY_NAME = "theme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeRepository themeRepository;

    public ThemeResource(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    /**
     * {@code POST  /themes} : Create a new theme.
     *
     * @param theme the theme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new theme, or with status {@code 400 (Bad Request)} if the theme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/themes")
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) throws URISyntaxException {
        log.debug("REST request to save Theme : {}", theme);
        if (theme.getId() != null) {
            throw new BadRequestAlertException("A new theme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Theme result = themeRepository.save(theme);
        return ResponseEntity
            .created(new URI("/api/themes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /themes/:id} : Updates an existing theme.
     *
     * @param id the id of the theme to save.
     * @param theme the theme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theme,
     * or with status {@code 400 (Bad Request)} if the theme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/themes/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable(value = "id", required = false) final Long id, @RequestBody Theme theme)
        throws URISyntaxException {
        log.debug("REST request to update Theme : {}, {}", id, theme);
        if (theme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, theme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Theme result = themeRepository.save(theme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, theme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /themes/:id} : Partial updates given fields of an existing theme, field will ignore if it is null
     *
     * @param id the id of the theme to save.
     * @param theme the theme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theme,
     * or with status {@code 400 (Bad Request)} if the theme is not valid,
     * or with status {@code 404 (Not Found)} if the theme is not found,
     * or with status {@code 500 (Internal Server Error)} if the theme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/themes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Theme> partialUpdateTheme(@PathVariable(value = "id", required = false) final Long id, @RequestBody Theme theme)
        throws URISyntaxException {
        log.debug("REST request to partial update Theme partially : {}, {}", id, theme);
        if (theme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, theme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Theme> result = themeRepository
            .findById(theme.getId())
            .map(existingTheme -> {
                if (theme.getLabel() != null) {
                    existingTheme.setLabel(theme.getLabel());
                }
                if (theme.getDescription() != null) {
                    existingTheme.setDescription(theme.getDescription());
                }

                return existingTheme;
            })
            .map(themeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, theme.getId().toString())
        );
    }

    /**
     * {@code GET  /themes} : get all the themes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themes in body.
     */
    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> getAllThemes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Themes");
        Page<Theme> page = themeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /themes/:id} : get the "id" theme.
     *
     * @param id the id of the theme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the theme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/themes/{id}")
    public ResponseEntity<Theme> getTheme(@PathVariable Long id) {
        log.debug("REST request to get Theme : {}", id);
        Optional<Theme> theme = themeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(theme);
    }

    /**
     * {@code DELETE  /themes/:id} : delete the "id" theme.
     *
     * @param id the id of the theme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        log.debug("REST request to delete Theme : {}", id);
        themeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
