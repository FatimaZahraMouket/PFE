package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Programme.
 */
@Entity
@Table(name = "programme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "heure")
    private String heure;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "programme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "planning", "programme" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "programmes", "photos", "offre" }, allowSetters = true)
    private Planning planning;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Programme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeure() {
        return this.heure;
    }

    public Programme heure(String heure) {
        this.setHeure(heure);
        return this;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDescription() {
        return this.description;
    }

    public Programme description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setProgramme(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setProgramme(this));
        }
        this.photos = photos;
    }

    public Programme photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Programme addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setProgramme(this);
        return this;
    }

    public Programme removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setProgramme(null);
        return this;
    }

    public Planning getPlanning() {
        return this.planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Programme planning(Planning planning) {
        this.setPlanning(planning);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Programme)) {
            return false;
        }
        return id != null && id.equals(((Programme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programme{" +
            "id=" + getId() +
            ", heure='" + getHeure() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
