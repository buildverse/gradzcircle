
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
@Table(name = "candidate_applied_jobs")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "candidate_applied_jobs")
public class CandidateAppliedJobs implements Serializable {

	@EmbeddedId
	private CandidateJobId id;

	public CandidateAppliedJobs() {

	}

	public CandidateAppliedJobs(Candidate candidate, Job job) {
		this.id = new CandidateJobId(candidate.getId(), job.getId());
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

	@Embeddable
	public static class CandidateJobId implements Serializable {

		@Column(name = "candidate_id")
		private Long candidateId;

		@Column(name = "job_id")
		private Long jobId;

		public CandidateJobId(Long candidateId, Long jobId) {
			this.candidateId = candidateId;
			this.jobId = jobId;
		}

		public CandidateJobId() {

		}

		/**
		 * @return the candidateId
		 */
		public Long getCandidateId() {
			return candidateId;
		}

		/**
		 * @param candidateId
		 *            the candidateId to set
		 */
		public void setCandidateId(Long candidateId) {
			this.candidateId = candidateId;
		}

		/**
		 * @return the jobId
		 */
		public Long getJobId() {
			return jobId;
		}

		/**
		 * @param jobId
		 *            the jobId to set
		 */
		public void setJobId(Long jobId) {
			this.jobId = jobId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 * (Objects.hashCode(candidateId) + Objects.hashCode(jobId));
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
			if (candidateJobId.candidateId == null || candidateId == null || candidateJobId.jobId == null
					|| jobId == null) {
				return false;
			}
			return Objects.equals(candidateJobId.candidateId, candidateId)
					&& Objects.equals(candidateJobId.jobId, jobId);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CandidateJobId [candidateId=" + candidateId + ", jobId=" + jobId + " abd hascode is"
					+ this.hashCode() + "]";
		}

	}

}
