package com.drishika.gradzcircle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProfileCategory.
 */
@Entity
@Table(name = "profile_category")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "profilecategory")
public class ProfileCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "weightage")
    private Integer weightage;
    

    @OneToMany(mappedBy = "profileCategory",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "candidateProfile")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CandidateProfileScore> profileScores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ProfileCategory categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getWeightage() {
        return weightage;
    }

    public ProfileCategory weightage(Integer weightage) {
        this.weightage = weightage;
        return this;
    }

    public void setWeightage(Integer weightage) {
        this.weightage = weightage;
    }

    /**
   	 * @return the profileScores
   	 */
   	public Set<CandidateProfileScore> getProfileScores() {
   		return profileScores;
   	}

   	/**
   	 * @param profileScores the profileScores to set
   	 */
   	public void setProfileScores(Set<CandidateProfileScore> profileScores) {
   		this.profileScores = profileScores;
   	}
   	
   	public ProfileCategory addCandidateProfileScore (CandidateProfileScore profileScore) {
   		this.profileScores.add(profileScore);
   		profileScore.setProfileCategory(this);
   		return this;
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
        ProfileCategory profileCategory = (ProfileCategory) o;
        if (profileCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", weightage=" + getWeightage() +
            "}";
    }
}
