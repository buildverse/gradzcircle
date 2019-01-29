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

import com.drishika.gradzcircle.domain.CandidateJob.CandidateJobId;
import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * @author abhinav
 *
 */

@Entity
@Table(name = "candidate_profile_score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "CandidateProfileScore")
public class CandidateProfileScore implements Serializable  {
	
	@EmbeddedId
	private CandidateProfileScoreId id;
	
	@ManyToOne
	@JoinColumn(name = "candidates_id", insertable = false, updatable = false)
	@JsonBackReference
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "profile_categories_id", insertable = false, updatable = false)
	@JsonBackReference(value = "candidateProfile")
	private ProfileCategory profileCategory;
	
	@Column(name="score")
	private Double score;
	
	public CandidateProfileScore() {
		
	}
	
	public CandidateProfileScore(Candidate candidate, ProfileCategory profileCategory) {
		this.id = new CandidateProfileScoreId(candidate.getId(), profileCategory.getId());
		this.candidate = candidate;
		this.profileCategory = profileCategory;
	}
	
	/**
	 * @return the id
	 */
	public CandidateProfileScoreId getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(CandidateProfileScoreId id) {
		this.id = id;
	}


	/**
	 * @return the candidate
	 */
	public Candidate getCandidate() {
		return candidate;
	}


	/**
	 * @param candidate the candidate to set
	 */
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}


	/**
	 * @return the profileCategory
	 */
	public ProfileCategory getProfileCategory() {
		return profileCategory;
	}


	/**
	 * @param profileCategory the profileCategory to set
	 */
	public void setProfileCategory(ProfileCategory profileCategory) {
		this.profileCategory = profileCategory;
	}




	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}

	public int hashCode() {
		return 31 * (Objects.hashCode(getId().candidateId) + Objects.hashCode(getId().profileId));
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CandidateProfileScore candidateProfileScore = (CandidateProfileScore) obj;
		if (candidateProfileScore.getId().candidateId == null || getId().candidateId == null
				|| candidateProfileScore.getId().profileId == null || getId().profileId == null) {
			return false;
		}
		return Objects.equals(getId().candidateId, candidateProfileScore.getId().candidateId)
				&& Objects.equals(candidateProfileScore.getId().profileId, getId().profileId);
	}
	
	
	



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CandidateProfileScore [id=" + id + ", candidate=" + candidate + ", profileCategory=" + profileCategory
				+ ", score=" + score + "]";
	}






	@Embeddable
	public static class CandidateProfileScoreId implements Serializable {

		@Column(name = "candidates_id")
		private Long candidateId;

		@Column(name = "profile_categories_id")
		private Long profileId;

		public CandidateProfileScoreId(Long candidateId, Long profileId) {
			this.candidateId = candidateId;
			this.profileId = profileId;
		}

		public CandidateProfileScoreId() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 * (Objects.hashCode(candidateId) + Objects.hashCode(profileId));
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
			CandidateProfileScoreId candidateProfileScoreId = (CandidateProfileScoreId) obj;
			if (candidateProfileScoreId.candidateId == null || candidateId == null || candidateProfileScoreId.profileId == null
					|| profileId == null) {
				return false;
			}
			return Objects.equals(candidateProfileScoreId.candidateId, candidateId)
					&& Objects.equals(candidateProfileScoreId.profileId, profileId);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CandidateProfileScoreId [candidateId=" + candidateId + ", profileId=" + profileId + " abd hascode is"
					+ this.hashCode() + "]";
		}

	}

}
