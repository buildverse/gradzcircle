package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Employability.
 */
@Entity
@Table(name = "employability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employability")
public class Employability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "employable_skill_name")
    private String employableSkillName;

    @Column(name = "employability_score")
    private Integer employabilityScore;

    @Column(name = "employability_percentile")
    private Integer employabilityPercentile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployableSkillName() {
        return employableSkillName;
    }

    public Employability employableSkillName(String employableSkillName) {
        this.employableSkillName = employableSkillName;
        return this;
    }

    public void setEmployableSkillName(String employableSkillName) {
        this.employableSkillName = employableSkillName;
    }

    public Integer getEmployabilityScore() {
        return employabilityScore;
    }

    public Employability employabilityScore(Integer employabilityScore) {
        this.employabilityScore = employabilityScore;
        return this;
    }

    public void setEmployabilityScore(Integer employabilityScore) {
        this.employabilityScore = employabilityScore;
    }

    public Integer getEmployabilityPercentile() {
        return employabilityPercentile;
    }

    public Employability employabilityPercentile(Integer employabilityPercentile) {
        this.employabilityPercentile = employabilityPercentile;
        return this;
    }

    public void setEmployabilityPercentile(Integer employabilityPercentile) {
        this.employabilityPercentile = employabilityPercentile;
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
        Employability employability = (Employability) o;
        if (employability.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employability{" +
            "id=" + getId() +
            ", employableSkillName='" + getEmployableSkillName() + "'" +
            ", employabilityScore=" + getEmployabilityScore() +
            ", employabilityPercentile=" + getEmployabilityPercentile() +
            "}";
    }
}
