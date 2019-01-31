package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AppConfig.
 */
@Entity
@Table(name = "app_config")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appconfig")
public class AppConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "config_name")
    private String configName;

    @Column(name = "config_value")
    private Boolean configValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public AppConfig configName(String configName) {
        this.configName = configName;
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Boolean isConfigValue() {
        return configValue;
    }

    public AppConfig configValue(Boolean configValue) {
        this.configValue = configValue;
        return this;
    }

    public void setConfigValue(Boolean configValue) {
        this.configValue = configValue;
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
        AppConfig appConfig = (AppConfig) o;
        if (appConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppConfig{" +
            "id=" + getId() +
            ", configName='" + getConfigName() + "'" +
            ", configValue='" + isConfigValue() + "'" +
            "}";
    }
}
