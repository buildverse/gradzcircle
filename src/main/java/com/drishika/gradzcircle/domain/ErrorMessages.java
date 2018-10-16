package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ErrorMessages.
 */
@Entity
@Table(name = "error_messages")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "errormessages")
public class ErrorMessages implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "component_name")
	private String componentName;

	@Column(name = "error_key")
	private String errorKey;

	@Column(name = "error_message")
	private String errorMessage;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComponentName() {
		return componentName;
	}

	public ErrorMessages componentName(String componentName) {
		this.componentName = componentName;
		return this;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public ErrorMessages errorKey(String errorKey) {
		this.errorKey = errorKey;
		return this;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public ErrorMessages errorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ErrorMessages errorMessages = (ErrorMessages) o;
		if (errorMessages.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), errorMessages.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ErrorMessages{" + "id=" + getId() + ", componentName='" + getComponentName() + "'" + ", errorKey='"
				+ getErrorKey() + "'" + ", errorMessage='" + getErrorMessage() + "'" + "}";
	}
}
