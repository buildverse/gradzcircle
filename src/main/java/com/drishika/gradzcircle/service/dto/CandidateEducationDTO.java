/**
 * 
 */
package com.drishika.gradzcircle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author abhinav
 *
 */
public class CandidateEducationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double grade;

	private LocalDate educationFromDate;

	private LocalDate educationToDate;

	private Boolean isPursuingEducation;

	private Integer gradeScale;

	private Boolean highestQualification;

	private Integer roundOfGrade;

	private Integer gradeDecimal;

	private Double percentage;

	private String scoreType;

	private Integer educationDuration;

	private String collegeName;

	private String universityName;

	private Double score;
	
	private Long id;
	
	private CollegeDTO college;
	
	private UniversityDTO university;
	
	private CourseDTO course;
	
	private QualificationDTO qualification;

	private Set<CandidateProjectDTO> projects = new HashSet<>();
	
	private CandidateDTO candidate;
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the grade
	 */
	public Double getGrade() {
		return grade;
	}

	/**
	 * @param grade
	 *            the grade to set
	 */
	public void setGrade(Double grade) {
		this.grade = grade;
	}

	/**
	 * @return the educationFromDate
	 */
	public LocalDate getEducationFromDate() {
		return educationFromDate;
	}

	/**
	 * @param educationFromDate
	 *            the educationFromDate to set
	 */
	public void setEducationFromDate(LocalDate educationFromDate) {
		this.educationFromDate = educationFromDate;
	}

	/**
	 * @return the educationToDate
	 */
	public LocalDate getEducationToDate() {
		return educationToDate;
	}

	/**
	 * @param educationToDate
	 *            the educationToDate to set
	 */
	public void setEducationToDate(LocalDate educationToDate) {
		this.educationToDate = educationToDate;
	}

	/**
	 * @return the isPursuingEducation
	 */
	public Boolean getIsPursuingEducation() {
		return isPursuingEducation;
	}

	/**
	 * @param isPursuingEducation
	 *            the isPursuingEducation to set
	 */
	public void setIsPursuingEducation(Boolean isPursuingEducation) {
		this.isPursuingEducation = isPursuingEducation;
	}

	/**
	 * @return the gradeScale
	 */
	public Integer getGradeScale() {
		return gradeScale;
	}

	/**
	 * @param gradeScale
	 *            the gradeScale to set
	 */
	public void setGradeScale(Integer gradeScale) {
		this.gradeScale = gradeScale;
	}

	/**
	 * @return the highestQualification
	 */
	public Boolean getHighestQualification() {
		return highestQualification;
	}

	/**
	 * @param highestQualification
	 *            the highestQualification to set
	 */
	public void setHighestQualification(Boolean highestQualification) {
		this.highestQualification = highestQualification;
	}

	/**
	 * @return the roundOfGrade
	 */
	public Integer getRoundOfGrade() {
		return roundOfGrade;
	}

	/**
	 * @param roundOfGrade
	 *            the roundOfGrade to set
	 */
	public void setRoundOfGrade(Integer roundOfGrade) {
		this.roundOfGrade = roundOfGrade;
	}

	/**
	 * @return the gradeDecimal
	 */
	public Integer getGradeDecimal() {
		return gradeDecimal;
	}

	/**
	 * @param gradeDecimal
	 *            the gradeDecimal to set
	 */
	public void setGradeDecimal(Integer gradeDecimal) {
		this.gradeDecimal = gradeDecimal;
	}

	
	/**
	 * @return the percentage
	 */
	public Double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the scoreType
	 */
	public String getScoreType() {
		return scoreType;
	}

	/**
	 * @param scoreType
	 *            the scoreType to set
	 */
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * @return the educationDuration
	 */
	public Integer getEducationDuration() {
		return educationDuration;
	}

	/**
	 * @param educationDuration
	 *            the educationDuration to set
	 */
	public void setEducationDuration(Integer educationDuration) {
		this.educationDuration = educationDuration;
	}

	/**
	 * @return the projects
	 */
	public Set<CandidateProjectDTO> getProjects() {
		return projects;
	}

	/**
	 * @param projects
	 *            the projects to set
	 */
	public void setProjects(Set<CandidateProjectDTO> projects) {
		this.projects = projects;
	}

	/**
	 * @return the collegeName
	 */
	public String getCollegeName() {
		return collegeName;
	}

	/**
	 * @param collegeName
	 *            the collegeName to set
	 */
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	/**
	 * @return the universityName
	 */
	public String getUniversityName() {
		return universityName;
	}

	/**
	 * @param universityName
	 *            the universityName to set
	 */
	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}


	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}

	/**
	 * @return the candidate
	 */
	public CandidateDTO getCandidate() {
		return candidate;
	}

	/**
	 * @param candidate the candidate to set
	 */
	public void setCandidate(CandidateDTO candidate) {
		this.candidate = candidate;
	}

	/**
	 * @return the college
	 */
	public CollegeDTO getCollege() {
		return college;
	}

	/**
	 * @param college the college to set
	 */
	public void setCollege(CollegeDTO college) {
		this.college = college;
	}

	/**
	 * @return the university
	 */
	public UniversityDTO getUniversity() {
		return university;
	}

	/**
	 * @param university the university to set
	 */
	public void setUniversity(UniversityDTO university) {
		this.university = university;
	}

	/**
	 * @return the course
	 */
	public CourseDTO getCourse() {
		return course;
	}

	/**
	 * @param course the course to set
	 */
	public void setCourse(CourseDTO course) {
		this.course = course;
	}

	/**
	 * @return the qualification
	 */
	public QualificationDTO getQualification() {
		return qualification;
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(QualificationDTO qualification) {
		this.qualification = qualification;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CandidateEducationDTO [grade=" + grade + ", educationFromDate=" + educationFromDate
				+ ", educationToDate=" + educationToDate + ", isPursuingEducation=" + isPursuingEducation
				+ ", gradeScale=" + gradeScale + ", highestQualification=" + highestQualification + ", roundOfGrade="
				+ roundOfGrade + ", gradeDecimal=" + gradeDecimal + ", percentage=" + percentage + ", scoreType="
				+ scoreType + ", educationDuration=" + educationDuration + ", collegeName=" + collegeName
				+ ", universityName=" + universityName + ", score=" + score + ", id=" + id + ", college=" + college
				+ ", university=" + university + ", course=" + course + ", qualification=" + qualification
				+ ", projects=" + projects + ", candidate=" + candidate + "]";
	}

	

}
