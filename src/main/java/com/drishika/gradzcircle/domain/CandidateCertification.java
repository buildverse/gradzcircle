package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CandidateCertification.
 */
@Entity
@Table(name = "candidate_certification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidatecertification")
public class CandidateCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "certification_title")
    private String certificationTitle;

    @Column(name = "certification_date")
    private LocalDate certificationDate;

    @Size(max = 5000)
    @Column(name = "certification_details", length = 5000)
    private String certificationDetails;

    @ManyToOne
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificationTitle() {
        return certificationTitle;
    }

    public CandidateCertification certificationTitle(String certificationTitle) {
        this.certificationTitle = certificationTitle;
        return this;
    }

    public void setCertificationTitle(String certificationTitle) {
        this.certificationTitle = certificationTitle;
    }

    public LocalDate getCertificationDate() {
        return certificationDate;
    }

    public CandidateCertification certificationDate(LocalDate certificationDate) {
        this.certificationDate = certificationDate;
        return this;
    }

    public void setCertificationDate(LocalDate certificationDate) {
        this.certificationDate = certificationDate;
    }

    public String getCertificationDetails() {
        return certificationDetails;
    }

    public CandidateCertification certificationDetails(String certificationDetails) {
        this.certificationDetails = certificationDetails;
        return this;
    }

    public void setCertificationDetails(String certificationDetails) {
        this.certificationDetails = certificationDetails;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public CandidateCertification candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
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
        CandidateCertification candidateCertification = (CandidateCertification) o;
        if (candidateCertification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidateCertification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CandidateCertification{" +
            "id=" + getId() +
            ", certificationTitle='" + getCertificationTitle() + "'" +
            ", certificationDate='" + getCertificationDate() + "'" +
            ", certificationDetails='" + getCertificationDetails() + "'" +
            "}";
    }
}
