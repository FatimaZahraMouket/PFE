package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pays.
 */
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "reviews")
    private Integer reviews;

    @Column(name = "continent")
    private String continent;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_saved")
    private Boolean isSaved;

    @Column(name = "is_liked")
    private Boolean isLiked;

    @OneToMany(mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plannings", "themes", "badges", "pays" }, allowSetters = true)
    private Set<Offre> offres = new HashSet<>();

    @OneToMany(mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "planning", "programme" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pays id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Pays label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Pays description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReviews() {
        return this.reviews;
    }

    public Pays reviews(Integer reviews) {
        this.setReviews(reviews);
        return this;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getContinent() {
        return this.continent;
    }

    public Pays continent(String continent) {
        this.setContinent(continent);
        return this;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Pays latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Pays longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsSaved() {
        return this.isSaved;
    }

    public Pays isSaved(Boolean isSaved) {
        this.setIsSaved(isSaved);
        return this;
    }

    public void setIsSaved(Boolean isSaved) {
        this.isSaved = isSaved;
    }

    public Boolean getIsLiked() {
        return this.isLiked;
    }

    public Pays isLiked(Boolean isLiked) {
        this.setIsLiked(isLiked);
        return this;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Set<Offre> getOffres() {
        return this.offres;
    }

    public void setOffres(Set<Offre> offres) {
        if (this.offres != null) {
            this.offres.forEach(i -> i.setPays(null));
        }
        if (offres != null) {
            offres.forEach(i -> i.setPays(this));
        }
        this.offres = offres;
    }

    public Pays offres(Set<Offre> offres) {
        this.setOffres(offres);
        return this;
    }

    public Pays addOffre(Offre offre) {
        this.offres.add(offre);
        offre.setPays(this);
        return this;
    }

    public Pays removeOffre(Offre offre) {
        this.offres.remove(offre);
        offre.setPays(null);
        return this;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setPays(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setPays(this));
        }
        this.photos = photos;
    }

    public Pays photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Pays addPhoto(Photo photo) {
        this.photos.add(photo);
        photo.setPays(this);
        return this;
    }

    public Pays removePhoto(Photo photo) {
        this.photos.remove(photo);
        photo.setPays(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pays)) {
            return false;
        }
        return id != null && id.equals(((Pays) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", reviews=" + getReviews() +
            ", continent='" + getContinent() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", isSaved='" + getIsSaved() + "'" +
            ", isLiked='" + getIsLiked() + "'" +
            "}";
    }
}
