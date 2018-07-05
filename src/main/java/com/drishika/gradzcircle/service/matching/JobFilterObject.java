/**
 * 
 */
package com.drishika.gradzcircle.service.matching;


import java.util.List;

import com.drishika.gradzcircle.domain.College;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.Gender;
import com.drishika.gradzcircle.domain.Language;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.University;

/**
 * @author abhinav
 *
 */
public class JobFilterObject {
	
	private List<Language> languages;
	private List <College> colleges;
	private List<University> universities;
	private List<Qualification> qualifications;
	private List<Course> courses;
	private Gender gender;
	private GraduationDate graduationDate;
	private GraduationDate graduationFromDate;
	private GraduationDate graduationToDate;
	private Boolean basic;
	private Boolean premium;
	private Boolean addOn;
	private String scoreType;
	private String graduationDateType;
	private Double gpa;
	private Double percent;
	
	/**
	 * @return the basic
	 */
	public Boolean getBasic() {
		return basic;
	}
	/**
	 * @param basic the basic to set
	 */
	public void setBasic(Boolean basic) {
		this.basic = basic;
	}
	/**
	 * @return the premium
	 */
	public Boolean getPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	public void setPremium(Boolean premium) {
		this.premium = premium;
	}
	/**
	 * @return the addOn
	 */
	public Boolean getAddOn() {
		return addOn;
	}
	/**
	 * @param addOn the addOn to set
	 */
	public void setAddOn(Boolean addOn) {
		this.addOn = addOn;
	}
	/**
	 * @return the scoreType
	 */
	public String getScoreType() {
		return scoreType;
	}
	/**
	 * @param scoreType the scoreType to set
	 */
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	/**
	 * @return the graduationDateType
	 */
	public String getGraduationDateType() {
		return graduationDateType;
	}
	/**
	 * @param graduationDateType the graduationDateType to set
	 */
	public void setGraduationDateType(String graduationDateType) {
		this.graduationDateType = graduationDateType;
	}
	/**
	 * @return the languages
	 */
	public List<Language> getLanguages() {
		return languages;
	}
	/**
	 * @param languages the languages to set
	 */
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
	/**
	 * @return the colleges
	 */
	public List<College> getColleges() {
		return colleges;
	}
	/**
	 * @param colleges the colleges to set
	 */
	public void setColleges(List<College> colleges) {
		this.colleges = colleges;
	}
	/**
	 * @return the universities
	 */
	public List<University> getUniversities() {
		return universities;
	}
	/**
	 * @param universities the universities to set
	 */
	public void setUniversities(List<University> universities) {
		this.universities = universities;
	}
	/**
	 * @return the qualifications
	 */
	public List<Qualification> getQualifications() {
		return qualifications;
	}
	/**
	 * @param qualifications the qualifications to set
	 */
	public void setQualifications(List<Qualification> qualifications) {
		this.qualifications = qualifications;
	}
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	/**
	
	/**
	 * @return the graduationFromDate
	 */
	public GraduationDate getGraduationFromDate() {
		return graduationFromDate;
	}
	/**
	 * @param graduationFromDate the graduationFromDate to set
	 */
	public void setGraduationFromDate(GraduationDate graduationFromDate) {
		this.graduationFromDate = graduationFromDate;
	}
	/**
	 * @return the graduationToDate
	 */
	public GraduationDate getGraduationToDate() {
		return graduationToDate;
	}
	/**
	 * @param graduationToDate the graduationToDate to set
	 */
	public void setGraduationToDate(GraduationDate graduationToDate) {
		this.graduationToDate = graduationToDate;
	}
	
	
	/**
	 * @return the graduationDate
	 */
	public GraduationDate getGraduationDate() {
		return graduationDate;
	}
	/**
	 * @param graduationDate the graduationDate to set
	 */
	public void setGraduationDate(GraduationDate graduationDate) {
		this.graduationDate = graduationDate;
	}
	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}
	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	
	
	/**
	 * @return the gpa
	 */
	public Double getGpa() {
		return gpa;
	}
	/**
	 * @param gpa the gpa to set
	 */
	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}
	/**
	 * @return the percent
	 */
	public Double getPercent() {
		return percent;
	}
	/**
	 * @param percent the percent to set
	 */
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addOn == null) ? 0 : addOn.hashCode());
		result = prime * result + ((basic == null) ? 0 : basic.hashCode());
		result = prime * result + ((colleges == null) ? 0 : colleges.hashCode());
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((gpa == null) ? 0 : gpa.hashCode());
		result = prime * result + ((graduationDate == null) ? 0 : graduationDate.hashCode());
		result = prime * result + ((graduationDateType == null) ? 0 : graduationDateType.hashCode());
		result = prime * result + ((graduationFromDate == null) ? 0 : graduationFromDate.hashCode());
		result = prime * result + ((graduationToDate == null) ? 0 : graduationToDate.hashCode());
		result = prime * result + ((languages == null) ? 0 : languages.hashCode());
		result = prime * result + ((percent == null) ? 0 : percent.hashCode());
		result = prime * result + ((premium == null) ? 0 : premium.hashCode());
		result = prime * result + ((qualifications == null) ? 0 : qualifications.hashCode());
		result = prime * result + ((scoreType == null) ? 0 : scoreType.hashCode());
		result = prime * result + ((universities == null) ? 0 : universities.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobFilterObject other = (JobFilterObject) obj;
		if (addOn == null) {
			if (other.addOn != null)
				return false;
		} else if (!addOn.equals(other.addOn))
			return false;
		if (basic == null) {
			if (other.basic != null)
				return false;
		} else if (!basic.equals(other.basic))
			return false;
		if (colleges == null) {
			if (other.colleges != null)
				return false;
		} else if (!colleges.equals(other.colleges))
			return false;
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (gpa == null) {
			if (other.gpa != null)
				return false;
		} else if (!gpa.equals(other.gpa))
			return false;
		if (graduationDate == null) {
			if (other.graduationDate != null)
				return false;
		} else if (!graduationDate.equals(other.graduationDate))
			return false;
		if (graduationDateType == null) {
			if (other.graduationDateType != null)
				return false;
		} else if (!graduationDateType.equals(other.graduationDateType))
			return false;
		if (graduationFromDate == null) {
			if (other.graduationFromDate != null)
				return false;
		} else if (!graduationFromDate.equals(other.graduationFromDate))
			return false;
		if (graduationToDate == null) {
			if (other.graduationToDate != null)
				return false;
		} else if (!graduationToDate.equals(other.graduationToDate))
			return false;
		if (languages == null) {
			if (other.languages != null)
				return false;
		} else if (!languages.equals(other.languages))
			return false;
		if (percent == null) {
			if (other.percent != null)
				return false;
		} else if (!percent.equals(other.percent))
			return false;
		if (premium == null) {
			if (other.premium != null)
				return false;
		} else if (!premium.equals(other.premium))
			return false;
		if (qualifications == null) {
			if (other.qualifications != null)
				return false;
		} else if (!qualifications.equals(other.qualifications))
			return false;
		if (scoreType == null) {
			if (other.scoreType != null)
				return false;
		} else if (!scoreType.equals(other.scoreType))
			return false;
		if (universities == null) {
			if (other.universities != null)
				return false;
		} else if (!universities.equals(other.universities))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JobFilterObject [languages=" + languages + ", colleges=" + colleges + ", universities=" + universities
				+ ", qualifications=" + qualifications + ", courses=" + courses + ", gender=" + gender
				
				+ ", graduationDate=" + graduationDate + ", graduationFromDate=" + graduationFromDate
				+ ", graduationToDate=" + graduationToDate + ", basic=" + basic + ", premium=" + premium + ", addOn="
				+ addOn + ", scoreType=" + scoreType + ", graduationDateType=" + graduationDateType + ", gpa=" + gpa
				+ ", percent=" + percent + "]";
	}
	
	

}
