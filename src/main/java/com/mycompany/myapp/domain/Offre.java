package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Language;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Offre.
 */
@Entity
@Table(name = "offre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Offre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "image")
    private String image;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @OneToMany(mappedBy = "offre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "programmes", "photos", "offre" }, allowSetters = true)
    private Set<Planning> plannings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_offre__themes",
        joinColumns = @JoinColumn(name = "offre_id"),
        inverseJoinColumns = @JoinColumn(name = "themes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "offres" }, allowSetters = true)
    private Set<Theme> themes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_offre__badges",
        joinColumns = @JoinColumn(name = "offre_id"),
        inverseJoinColumns = @JoinColumn(name = "badges_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "offres" }, allowSetters = true)
    private Set<Badge> badges = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "offres", "photos" }, allowSetters = true)
    private Pays pays;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Offre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Offre label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Offre description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Offre prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getImage() {
        return this.image;
    }

    public Offre image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Offre latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Offre longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Offre language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Planning> getPlannings() {
        return this.plannings;
    }

    public void setPlannings(Set<Planning> plannings) {
        if (this.plannings != null) {
            this.plannings.forEach(i -> i.setOffre(null));
        }
        if (plannings != null) {
            plannings.forEach(i -> i.setOffre(this));
        }
        this.plannings = plannings;
    }

    public Offre plannings(Set<Planning> plannings) {
        this.setPlannings(plannings);
        return this;
    }

    public Offre addPlanning(Planning planning) {
        this.plannings.add(planning);
        planning.setOffre(this);
        return this;
    }

    public Offre removePlanning(Planning planning) {
        this.plannings.remove(planning);
        planning.setOffre(null);
        return this;
    }

    public Set<Theme> getThemes() {
        return this.themes;
    }

    public void setThemes(Set<Theme> themes) {
        this.themes = themes;
    }

    public Offre themes(Set<Theme> themes) {
        this.setThemes(themes);
        return this;
    }

    public Offre addThemes(Theme theme) {
        this.themes.add(theme);
        theme.getOffres().add(this);
        return this;
    }

    public Offre removeThemes(Theme theme) {
        this.themes.remove(theme);
        theme.getOffres().remove(this);
        return this;
    }

    public Set<Badge> getBadges() {
        return this.badges;
    }

    public void setBadges(Set<Badge> badges) {
        this.badges = badges;
    }

    public Offre badges(Set<Badge> badges) {
        this.setBadges(badges);
        return this;
    }

    public Offre addBadges(Badge badge) {
        this.badges.add(badge);
        badge.getOffres().add(this);
        return this;
    }

    public Offre removeBadges(Badge badge) {
        this.badges.remove(badge);
        badge.getOffres().remove(this);
        return this;
    }

    public Pays getPays() {
        return this.pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Offre pays(Pays pays) {
        this.setPays(pays);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Offre)) {
            return false;
        }
        return id != null && id.equals(((Offre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Offre{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", prix=" + getPrix() +
            ", image='" + getImage() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
