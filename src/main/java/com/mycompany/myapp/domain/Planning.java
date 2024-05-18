package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Planning.
 */
@Entity
@Table(name = "planning")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Planning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "jour_numero")
    private Integer jourNumero;

    @OneToMany(mappedBy = "planning")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "photos", "planning" }, allowSetters = true)
    private Set<Programme> programmes = new HashSet<>();

    @OneToMany(mappedBy = "planning")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "planning", "programme" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "plannings", "themes", "badges", "pays" }, allowSetters = true)
    private Offre offre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Planning id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Planning titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Planning description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getJourNumero() {
        return this.jourNumero;
    }

    public Planning jourNumero(Integer jourNumero) {
        this.setJourNumero(jourNumero);
        return this;
    }

    public void setJourNumero(Integer jourNumero) {
        this.jourNumero = jourNumero;
    }

    public Set<Programme> getProgrammes() {
        return this.programmes;
    }

    public void setProgrammes(Set<Programme> programmes) {
        if (this.programmes != null) {
            this.programmes.forEach(i -> i.setPlanning(null));
        }
        if (programmes != null) {
            programmes.forEach(i -> i.setPlanning(this));
        }
        this.programmes = programmes;
    }

    public Planning programmes(Set<Programme> programmes) {
        this.setProgrammes(programmes);
        return this;
    }

    public Planning addProgramme(Programme programme) {
        this.programmes.add(programme);
        programme.setPlanning(this);
        return this;
    }

    public Planning removeProgramme(Programme programme) {
        this.programmes.remove(programme);
        programme.setPlanning(null);
        return this;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setPlanning(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setPlanning(this));
        }
        this.photos = photos;
    }

    public Planning photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Planning addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setPlanning(this);
        return this;
    }

    public Planning removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setPlanning(null);
        return this;
    }

    public Offre getOffre() {
        return this.offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Planning offre(Offre offre) {
        this.setOffre(offre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planning)) {
            return false;
        }
        return id != null && id.equals(((Planning) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Planning{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", jourNumero=" + getJourNumero() +
            "}";
    }
}
