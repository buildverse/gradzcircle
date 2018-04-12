package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Filter.
 */
@Entity
@Table(name = "filter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "filter")
public class Filter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "filter_name")
    private String filterName;

    @Column(name = "filter_cost")
    private Double filterCost;

    @Column(name = "comments")
    private String comments;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public Filter filterName(String filterName) {
        this.filterName = filterName;
        return this;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Double getFilterCost() {
        return filterCost;
    }

    public Filter filterCost(Double filterCost) {
        this.filterCost = filterCost;
        return this;
    }

    public void setFilterCost(Double filterCost) {
        this.filterCost = filterCost;
    }

    public String getComments() {
        return comments;
    }

    public Filter comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        Filter filter = (Filter) o;
        if (filter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), filter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Filter{" +
            "id=" + getId() +
            ", filterName='" + getFilterName() + "'" +
            ", filterCost='" + getFilterCost() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
