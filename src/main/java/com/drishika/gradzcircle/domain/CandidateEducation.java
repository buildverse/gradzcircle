package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CandidateEducation.
 */
@Entity
@Table(name = "candidate_education")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidateeducation")
public class CandidateEducation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "education_from_date")
    private LocalDate educationFromDate;

    @Column(name = "education_to_date")
    private LocalDate educationToDate;

    @Column(name = "is_pursuing_education")
    private Boolean isPursuingEducation;

    @Column(name = "grade_scale")
    private Integer gradeScale;

    @Column(name = "highest_qualification")
    private Boolean highestQualification;

    @Column(name = "round_of_grade")
    private Integer roundOfGrade;

    @Column(name = "grade_decimal")
    private Integer gradeDecimal;

    @Column(name = "captured_course")
    private String capturedCourse;

    @Column(name = "captured_qualification")
    private String capturedQualification;

    @Column(name = "captured_college")
    private String capturedCollege;

    @Column(name = "captured_university")
    private String capturedUniversity;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "score_type")
    private String scoreType;

    @Column(name = "education_duration")
    private Integer educationDuration;

    @ManyToOne
    private Candidate candidate;

    @OneToMany(mappedBy = "education", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateProject> projects = new HashSet<>();

    @ManyToOne
    private Qualification qualification;

    @ManyToOne
    private Course course;

    @ManyToOne
    private College college;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public CandidateEducation grade(Double grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public LocalDate getEducationFromDate() {
        return educationFromDate;
    }

    public CandidateEducation educationFromDate(LocalDate educationFromDate) {
        this.educationFromDate = educationFromDate;
        return this;
    }

    public void setEducationFromDate(LocalDate educationFromDate) {
        this.educationFromDate = educationFromDate;
    }

    public LocalDate getEducationToDate() {
        return educationToDate;
    }

    public CandidateEducation educationToDate(LocalDate educationToDate) {
        this.educationToDate = educationToDate;
        return this;
    }

    public void setEducationToDate(LocalDate educationToDate) {
        this.educationToDate = educationToDate;
    }

    public Boolean isIsPursuingEducation() {
        return isPursuingEducation;
    }

    public CandidateEducation isPursuingEducation(Boolean isPursuingEducation) {
        this.isPursuingEducation = isPursuingEducation;
        return this;
    }

    public void setIsPursuingEducation(Boolean isPursuingEducation) {
        this.isPursuingEducation = isPursuingEducation;
    }

    public Integer getGradeScale() {
        return gradeScale;
    }

    public CandidateEducation gradeScale(Integer gradeScale) {
        this.gradeScale = gradeScale;
        return this;
    }

    public void setGradeScale(Integer gradeScale) {
        this.gradeScale = gradeScale;
    }

    public Boolean isHighestQualification() {
        return highestQualification;
    }

    public CandidateEducation highestQualification(Boolean highestQualification) {
        this.highestQualification = highestQualification;
        return this;
    }

    public void setHighestQualification(Boolean highestQualification) {
        this.highestQualification = highestQualification;
    }

    public Integer getRoundOfGrade() {
        return roundOfGrade;
    }

    public CandidateEducation roundOfGrade(Integer roundOfGrade) {
        this.roundOfGrade = roundOfGrade;
        return this;
    }

    public void setRoundOfGrade(Integer roundOfGrade) {
        this.roundOfGrade = roundOfGrade;
    }

    public Integer getGradeDecimal() {
        return gradeDecimal;
    }

    public CandidateEducation gradeDecimal(Integer gradeDecimal) {
        this.gradeDecimal = gradeDecimal;
        return this;
    }

    public void setGradeDecimal(Integer gradeDecimal) {
        this.gradeDecimal = gradeDecimal;
    }

    public String getCapturedCourse() {
        return capturedCourse;
    }

    public CandidateEducation capturedCourse(String capturedCourse) {
        this.capturedCourse = capturedCourse;
        return this;
    }

    public void setCapturedCourse(String capturedCourse) {
        this.capturedCourse = capturedCourse;
    }

    public String getCapturedQualification() {
        return capturedQualification;
    }

    public CandidateEducation capturedQualification(String capturedQualification) {
        this.capturedQualification = capturedQualification;
        return this;
    }

    public void setCapturedQualification(String capturedQualification) {
        this.capturedQualification = capturedQualification;
    }

    public String getCapturedCollege() {
        return capturedCollege;
    }

    public CandidateEducation capturedCollege(String capturedCollege) {
        this.capturedCollege = capturedCollege;
        return this;
    }

    public void setCapturedCollege(String capturedCollege) {
        this.capturedCollege = capturedCollege;
    }

    public String getCapturedUniversity() {
        return capturedUniversity;
    }

    public CandidateEducation capturedUniversity(String capturedUniversity) {
        this.capturedUniversity = capturedUniversity;
        return this;
    }

    public void setCapturedUniversity(String capturedUniversity) {
        this.capturedUniversity = capturedUniversity;
    }

    public Double getPercentage() {
        return percentage;
    }

    public CandidateEducation percentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getScoreType() {
        return scoreType;
    }

    public CandidateEducation scoreType(String scoreType) {
        this.scoreType = scoreType;
        return this;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public Integer getEducationDuration() {
        return educationDuration;
    }

    public CandidateEducation educationDuration(Integer educationDuration) {
        this.educationDuration = educationDuration;
        return this;
    }

    public void setEducationDuration(Integer educationDuration) {
        this.educationDuration = educationDuration;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public CandidateEducation candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Set<CandidateProject> getProjects() {
        return projects;
    }

    public CandidateEducation projects(Set<CandidateProject> candidateProjects) {
        this.projects = candidateProjects;
        return this;
    }

    public CandidateEducation addProjects(CandidateProject candidateProject) {
        this.projects.add(candidateProject);
        candidateProject.setEducation(this);
        return this;
    }

    public CandidateEducation removeProjects(CandidateProject candidateProject) {
        this.projects.remove(candidateProject);
        candidateProject.setEducation(null);
        return this;
    }

    public void setProjects(Set<CandidateProject> candidateProjects) {
        this.projects = candidateProjects;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public CandidateEducation qualification(Qualification qualification) {
        this.qualification = qualification;
        return this;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Course getCourse() {
        return course;
    }

    public CandidateEducation course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public College getCollege() {
        return college;
    }

    public CandidateEducation college(College college) {
        this.college = college;
        return this;
    }

    public void setCollege(College college) {
        this.college = college;
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
        CandidateEducation candidateEducation = (CandidateEducation) o;
        if (candidateEducation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), candidateEducation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CandidateEducation{" +
            "id=" + getId() +
            ", grade='" + getGrade() + "'" +
            ", educationFromDate='" + getEducationFromDate() + "'" +
            ", educationToDate='" + getEducationToDate() + "'" +
            ", isPursuingEducation='" + isIsPursuingEducation() + "'" +
            ", gradeScale='" + getGradeScale() + "'" +
            ", highestQualification='" + isHighestQualification() + "'" +
            ", roundOfGrade='" + getRoundOfGrade() + "'" +
            ", gradeDecimal='" + getGradeDecimal() + "'" +
            ", capturedCourse='" + getCapturedCourse() + "'" +
            ", capturedQualification='" + getCapturedQualification() + "'" +
            ", capturedCollege='" + getCapturedCollege() + "'" +
            ", capturedUniversity='" + getCapturedUniversity() + "'" +
            ", percentage='" + getPercentage() + "'" +
            ", scoreType='" + getScoreType() + "'" +
            ", educationDuration='" + getEducationDuration() + "'" +
            "}";
    }
}
