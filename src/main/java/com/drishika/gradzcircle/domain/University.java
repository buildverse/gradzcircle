package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A University.
 */
@Entity
@Table(name = "university")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class University implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "university_name")
    private String universityName;

    @ManyToOne
    private Country country;

    @OneToMany(mappedBy = "university")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<College> colleges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public University universityName(String universityName) {
        this.universityName = universityName;
        return this;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public Country getCountry() {
        return country;
    }

    public University country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<College> getColleges() {
        return colleges;
    }

    public University colleges(Set<College> colleges) {
        this.colleges = colleges;
        return this;
    }

    public University addCollege(College college) {
        this.colleges.add(college);
        college.setUniversity(this);
        return this;
    }

    public University removeCollege(College college) {
        this.colleges.remove(college);
        college.setUniversity(null);
        return this;
    }

    public void setColleges(Set<College> colleges) {
        this.colleges = colleges;
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
        University university = (University) o;
        if (university.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), university.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "University{" +
            "id=" + getId() +
            ", universityName='" + getUniversityName() + "'" +
            "}";
    }
}
