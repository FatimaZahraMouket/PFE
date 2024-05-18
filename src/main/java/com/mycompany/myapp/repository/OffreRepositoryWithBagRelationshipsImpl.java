package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Offre;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OffreRepositoryWithBagRelationshipsImpl implements OffreRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Offre> fetchBagRelationships(Optional<Offre> offre) {
        return offre.map(this::fetchThemes).map(this::fetchBadges);
    }

    @Override
    public Page<Offre> fetchBagRelationships(Page<Offre> offres) {
        return new PageImpl<>(fetchBagRelationships(offres.getContent()), offres.getPageable(), offres.getTotalElements());
    }

    @Override
    public List<Offre> fetchBagRelationships(List<Offre> offres) {
        return Optional.of(offres).map(this::fetchThemes).map(this::fetchBadges).orElse(Collections.emptyList());
    }

    Offre fetchThemes(Offre result) {
        return entityManager
            .createQuery("select offre from Offre offre left join fetch offre.themes where offre is :offre", Offre.class)
            .setParameter("offre", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Offre> fetchThemes(List<Offre> offres) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, offres.size()).forEach(index -> order.put(offres.get(index).getId(), index));
        List<Offre> result = entityManager
            .createQuery("select distinct offre from Offre offre left join fetch offre.themes where offre in :offres", Offre.class)
            .setParameter("offres", offres)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Offre fetchBadges(Offre result) {
        return entityManager
            .createQuery("select offre from Offre offre left join fetch offre.badges where offre is :offre", Offre.class)
            .setParameter("offre", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Offre> fetchBadges(List<Offre> offres) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, offres.size()).forEach(index -> order.put(offres.get(index).getId(), index));
        List<Offre> result = entityManager
            .createQuery("select distinct offre from Offre offre left join fetch offre.badges where offre in :offres", Offre.class)
            .setParameter("offres", offres)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
