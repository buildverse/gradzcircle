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
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "course")
    private String course;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateEducation> candidateEducations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public Course course(String course) {
        this.course = course;
        return this;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Set<CandidateEducation> getCandidateEducations() {
        return candidateEducations;
    }

    public Course candidateEducations(Set<CandidateEducation> candidateEducations) {
        this.candidateEducations = candidateEducations;
        return this;
    }

    public Course addCandidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducations.add(candidateEducation);
        candidateEducation.setCourse(this);
        return this;
    }

    public Course removeCandidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducations.remove(candidateEducation);
        candidateEducation.setCourse(null);
        return this;
    }

    public void setCandidateEducations(Set<CandidateEducation> candidateEducations) {
        this.candidateEducations = candidateEducations;
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
        Course course = (Course) o;
        if (course.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", course='" + getCourse() + "'" +
            "}";
    }
}
