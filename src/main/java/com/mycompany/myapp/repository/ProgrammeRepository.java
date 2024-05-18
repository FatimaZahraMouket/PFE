package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Programme;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Programme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Long> {}
