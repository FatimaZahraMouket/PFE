package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Planning;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Planning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanningRepository extends JpaRepository<Planning, Long> {}
