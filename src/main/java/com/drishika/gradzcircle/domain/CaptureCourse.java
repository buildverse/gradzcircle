package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CaptureCourse.
 */
@Entity
@Table(name = "capture_course")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "capturecourse")
public class CaptureCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @OneToOne
    @JoinColumn(unique = true)
    private CandidateEducation candidateEducation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public CaptureCourse courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CandidateEducation getCandidateEducation() {
        return candidateEducation;
    }

    public CaptureCourse candidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducation = candidateEducation;
        return this;
    }

    public void setCandidateEducation(CandidateEducation candidateEducation) {
        this.candidateEducation = candidateEducation;
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
        CaptureCourse captureCourse = (CaptureCourse) o;
        if (captureCourse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), captureCourse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CaptureCourse{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            "}";
    }
}
