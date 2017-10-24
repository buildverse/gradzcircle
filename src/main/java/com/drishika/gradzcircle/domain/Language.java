package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "language")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "language")
    private String language;

    @OneToMany(mappedBy = "language")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public Language language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<CandidateLanguageProficiency> getCandidateLanguageProficiencies() {
        return candidateLanguageProficiencies;
    }

    public Language candidateLanguageProficiencies(Set<CandidateLanguageProficiency> candidateLanguageProficiencies) {
        this.candidateLanguageProficiencies = candidateLanguageProficiencies;
        return this;
    }

    public Language addCandidateLanguageProficiency(CandidateLanguageProficiency candidateLanguageProficiency) {
        this.candidateLanguageProficiencies.add(candidateLanguageProficiency);
        candidateLanguageProficiency.setLanguage(this);
        return this;
    }

    public Language removeCandidateLanguageProficiency(CandidateLanguageProficiency candidateLanguageProficiency) {
        this.candidateLanguageProficiencies.remove(candidateLanguageProficiency);
        candidateLanguageProficiency.setLanguage(null);
        return this;
    }

    public void setCandidateLanguageProficiencies(Set<CandidateLanguageProficiency> candidateLanguageProficiencies) {
        this.candidateLanguageProficiencies = candidateLanguageProficiencies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Language language = (Language) o;
        if (language.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), language.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
