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
@Table(name = "corporate_candidate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "corporate_candidate")
public class CorporateCandidate implements Serializable {

	@EmbeddedId
	private CorporateCandidateJobId id;

	@ManyToOne
	@JoinColumn(name = "candidate_id", insertable = false, updatable = false)
	@JsonBackReference
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "corporate_id", insertable = false, updatable = false)
	@JsonBackReference(value = "candidateToCorporate")
	private Corporate corporate;

	public CorporateCandidate(Corporate corporate, Candidate candidate, Long jobId) {
		this.id = new CorporateCandidateJobId(corporate.getId(), candidate.getId(), jobId);
		this.candidate = candidate;
		this.corporate = corporate;
	}

	public CorporateCandidate() {

	}

	/**
	 * @return the id
	 */
	public CorporateCandidateJobId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(CorporateCandidateJobId id) {
		this.id = id;
	}

	public int hashCode() {
		return 31 * (Objects.hashCode(getId().candidateId) + Objects.hashCode(getId().corporateId)
				+ Objects.hashCode(id.jobId));
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CorporateCandidate corporateCandidate = (CorporateCandidate) obj;
		if (corporateCandidate.getId().candidateId == null || getId().candidateId == null
				|| corporateCandidate.getId().corporateId == null || getId().corporateId == null) {
			return false;
		}
		return Objects.equals(getId().candidateId, corporateCandidate.getId().candidateId)
				&& Objects.equals(corporateCandidate.getId().corporateId, getId().corporateId)
				&& Objects.equals(corporateCandidate.getId().jobId, getId().jobId);
	}

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
	 * @return the corporate
	 */
	public Corporate getCorporate() {
		return corporate;
	}

	/**
	 * @param corporate
	 *            the corporate to set
	 */
	public void setCorporate(Corporate corporate) {
		this.corporate = corporate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CorporateCandidate [candidate=" + candidate + ", corporate=" + corporate + ", job=" + id.jobId + "]";
	}

	@Embeddable
	public static class CorporateCandidateJobId implements Serializable {

		@Column(name = "candidate_id")
		private Long candidateId;

		@Column(name = "corporate_id")
		private Long corporateId;

		@Column(name = "job_id")
		private Long jobId;

		public CorporateCandidateJobId(Long corporateId, Long candidateId, Long jobId) {
			this.candidateId = candidateId;
			this.corporateId = corporateId;
			this.jobId = jobId;
		}

		public CorporateCandidateJobId() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 * (Objects.hashCode(candidateId) + Objects.hashCode(corporateId) + Objects.hashCode(jobId));
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
			CorporateCandidateJobId candidateCorporateId = (CorporateCandidateJobId) obj;
			if (candidateCorporateId.candidateId == null || candidateId == null
					|| candidateCorporateId.corporateId == null || corporateId == null
					|| candidateCorporateId.jobId == null || jobId == null) {
				return false;
			}
			return Objects.equals(candidateCorporateId.candidateId, candidateId)
					&& Objects.equals(candidateCorporateId.corporateId, corporateId)
					&& Objects.equals(candidateCorporateId.jobId, jobId);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CorporateCandidateJobId [candidateId=" + candidateId + ", corporateId=" + corporateId + ", jobId="
					+ jobId + "]";
		}

	}

}
