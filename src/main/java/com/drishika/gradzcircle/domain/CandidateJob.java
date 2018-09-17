/**
 * 
 */
package com.drishika.gradzcircle.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author abhinav
 *
 */

@Entity
@Table(name = "candidate_job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidate_job")
public class CandidateJob implements Serializable {

	@EmbeddedId
	private CandidateJobId id;

	@ManyToOne
	@JoinColumn(name = "candidates_id", insertable = false, updatable = false)
	@JsonBackReference
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "jobs_id", insertable = false, updatable = false)
	@JsonBackReference(value="jobToCandidate")
	private Job job;

	@Column(name = "match_score")
	private Double matchScore;
	
	@Column(name = "education_match_score")
	private Double educationMatchScore;
	
	@Column(name = "gender_match_score")
	private Double genderMatchScore;
	
	@Column(name = "language_match_score")
	private Double languageMatchScore;
	
	@Column(name = "total_eligible_score")
	private Double totalEligibleScore;

	public CandidateJob(Candidate candidate, Job job) {
		this.id = new CandidateJobId(candidate.getId(), job.getId());
		this.candidate = candidate;
		this.job = job;
	}
	
	public CandidateJob() {

	}

	/**
	 * @return the id
	 */
	public CandidateJobId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(CandidateJobId id) {
		this.id = id;
	}

	public int hashCode() {
		return 31*(Objects.hashCode(getId().candidateId) + Objects.hashCode(getId().jobId));
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CandidateJob candidateJob = (CandidateJob) obj;
		if (candidateJob.getId().candidateId == null || getId().candidateId == null || candidateJob.getId().jobId == null || getId().jobId==null) {
			return false;
		}
		return Objects.equals(getId().candidateId, candidateJob.getId().candidateId) && Objects.equals(candidateJob.getId().jobId, getId().jobId);
	}
	/*
	 * @Id
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="candidates_id")
	 * 
	 * @JsonBackReference private Candidate candidate;
	 * 
	 * @Id
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="jobs_id")
	 * 
	 * @JsonBackReference private Job job;
	 * 
	 * @Column(name = "match_score") private Double matchScore;
	 * 
	 * public CandidateJob() { }
	 * 
	 * public CandidateJob(Candidate candidate, Job job, Double matchScore) {
	 * this.candidate = candidate; this.job = job; this.matchScore = matchScore;
	 * job.addCandidateJob(this); candidate.addCandidateJob(this); }
	 * 
	 * 
	 * public Candidate getCandidate() { return candidate; }
	 * 
	 * 
	 * public void setCandidate(Candidate candidate) { this.candidate = candidate; }
	 * 
	 * 
	 * public Job getJob() { return job; }
	 * 
	 * 
	 * public void setJob(Job job) { this.job = job; }
	 * 
	 * 
	 * public Double getMatchScore() { return matchScore; }
	 * 
	 * 
	 * public void setMatchScore(Double matchScore) { this.matchScore = matchScore;
	 * }
	 * 
	 * 
	 * @Override public int hashCode() { final int prime = 31; int result = 1;
	 * result = prime * result + ((candidate == null) ? 0 : candidate.hashCode());
	 * result = prime * result + ((job == null) ? 0 : job.getId().hashCode());
	 * return result; }
	 * 
	 * 
	 * @Override public boolean equals(Object obj) { if (this == obj) return true;
	 * if (obj == null) return false; if (getClass() != obj.getClass()) return
	 * false; CandidateJob other = (CandidateJob) obj; if (candidate == null) { if
	 * (other.candidate != null) return false; } else if
	 * (!candidate.equals(other.candidate)) return false; if (job == null) { if
	 * (other.job != null) return false; } else if
	 * (!job.getId().equals(other.job.getId())) return false; return true; }
	 * 
	 * 
	 * @Override public String toString() { return "CandidateJob [candidate=" +
	 * candidate + ", job=" + job + ", matchScore=" + matchScore + "]"; }
	 * 
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */

	/**
	 * @return the candidate
	 */
	public Candidate getCandidate() {
		return candidate;
	}

	/**
	 * @param candidate
	 *            the candidate to set
	 */
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	/**
	 * @return the job
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(Job job) {
		this.job = job;
	}

	/**
	 * @return the matchScore
	 */
	public Double getMatchScore() {
		return matchScore;
	}

	/**
	 * @param matchScore
	 *            the matchScore to set
	 */
	public void setMatchScore(Double matchScore) {
		this.matchScore = matchScore;
	}
	
	
	
	/**
	 * @return the educationMatchScore
	 */
	public Double getEducationMatchScore() {
		return educationMatchScore;
	}

	/**
	 * @param educationMatchScore the educationMatchScore to set
	 */
	public void setEducationMatchScore(Double educationMatchScore) {
		this.educationMatchScore = educationMatchScore;
	}

	/**
	 * @return the genderMatchScore
	 */
	public Double getGenderMatchScore() {
		return genderMatchScore;
	}

	/**
	 * @param genderMatchScore the genderMatchScore to set
	 */
	public void setGenderMatchScore(Double genderMatchScore) {
		this.genderMatchScore = genderMatchScore;
	}

	/**
	 * @return the languageMatchScore
	 */
	public Double getLanguageMatchScore() {
		return languageMatchScore;
	}

	/**
	 * @param languageMatchScore the languageMatchScore to set
	 */
	public void setLanguageMatchScore(Double languageMatchScore) {
		this.languageMatchScore = languageMatchScore;
	}
	
	

	/**
	 * @return the totalEligibleScore
	 */
	public Double getTotalEligibleScore() {
		return totalEligibleScore;
	}

	/**
	 * @param totalEligibleScore the totalEligibleScore to set
	 */
	public void setTotalEligibleScore(Double totalEligibleScore) {
		this.totalEligibleScore = totalEligibleScore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CandidateJob [id=" + id + ", candidate=" + candidate + ", job=" + job + ", matchScore=" + matchScore
				+ ", educationMatchScore=" + educationMatchScore + ", genderMatchScore=" + genderMatchScore
				+ ", languageMatchScore=" + languageMatchScore + ", totalEligibleScore=" + totalEligibleScore + " hasCode="+this.hashCode()+"]";
	}

	@Embeddable
	public static class CandidateJobId implements Serializable {

		@Column(name = "candidates_id")
		private Long candidateId;

		@Column(name = "jobs_id")
		private Long jobId;

		public CandidateJobId(Long candidateId, Long jobId) {
			this.candidateId = candidateId;
			this.jobId = jobId;
		}

		public CandidateJobId() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 *(Objects.hashCode(candidateId) + Objects.hashCode(jobId));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			 if (this == obj) {
		            return true;
		        }
		        if (obj == null || getClass() != obj.getClass()) {
		            return false;
		        }
		        CandidateJobId candidateJobId = (CandidateJobId) obj;
		        if (candidateJobId.candidateId == null || candidateId == null || candidateJobId.jobId == null || jobId == null) {
		            return false;
		        }
		        return Objects.equals(candidateJobId.candidateId,candidateId) && Objects.equals(candidateJobId.jobId, jobId);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CandidateJobId [candidateId=" + candidateId + ", jobId=" + jobId + " abd hascode is"+ this.hashCode()+"]";
		}

	}

	

}
