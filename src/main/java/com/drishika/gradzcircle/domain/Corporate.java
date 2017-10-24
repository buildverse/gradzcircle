package com.drishika.gradzcircle.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Corporate.
 */
@Entity
@Table(name = "corporate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "corporate")
public class Corporate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "corporate_name")
    private String corporateName;

    @Column(name = "corporate_address")
    private String corporateAddress;

    @Column(name = "corporate_city")
    private String corporateCity;

    @Column(name = "established_since")
    private LocalDate establishedSince;

    @Column(name = "corporate_email")
    private String corporateEmail;

    @Column(name = "corporate_overview")
    private String corporateOverview;

    @Column(name = "corporate_benefits")
    private String corporateBenefits;

    @Column(name = "corporate_website")
    private String corporateWebsite;

    @Column(name = "corporate_facebook")
    private String corporateFacebook;

    @Column(name = "corporate_twitter")
    private String corporateTwitter;

    @Column(name = "corporate_instagram")
    private String corporateInstagram;

    @Column(name = "corporate_linked_in")
    private String corporateLinkedIn;

    @Column(name = "corporate_culture")
    private String corporateCulture;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "corporate_phone")
    private String corporatePhone;

    @Column(name = "corporate_phone_code")
    private String corporatePhoneCode;

    @Column(name = "contact_person_designation")
    private String contactPersonDesignation;

    @Column(name = "corporate_tag_line")
    private String corporateTagLine;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Industry industry;

    @OneToOne
    @JoinColumn(unique = true)
    private User login;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public Corporate corporateName(String corporateName) {
        this.corporateName = corporateName;
        return this;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateAddress() {
        return corporateAddress;
    }

    public Corporate corporateAddress(String corporateAddress) {
        this.corporateAddress = corporateAddress;
        return this;
    }

    public void setCorporateAddress(String corporateAddress) {
        this.corporateAddress = corporateAddress;
    }

    public String getCorporateCity() {
        return corporateCity;
    }

    public Corporate corporateCity(String corporateCity) {
        this.corporateCity = corporateCity;
        return this;
    }

    public void setCorporateCity(String corporateCity) {
        this.corporateCity = corporateCity;
    }

    public LocalDate getEstablishedSince() {
        return establishedSince;
    }

    public Corporate establishedSince(LocalDate establishedSince) {
        this.establishedSince = establishedSince;
        return this;
    }

    public void setEstablishedSince(LocalDate establishedSince) {
        this.establishedSince = establishedSince;
    }

    public String getCorporateEmail() {
        return corporateEmail;
    }

    public Corporate corporateEmail(String corporateEmail) {
        this.corporateEmail = corporateEmail;
        return this;
    }

    public void setCorporateEmail(String corporateEmail) {
        this.corporateEmail = corporateEmail;
    }

    public String getCorporateOverview() {
        return corporateOverview;
    }

    public Corporate corporateOverview(String corporateOverview) {
        this.corporateOverview = corporateOverview;
        return this;
    }

    public void setCorporateOverview(String corporateOverview) {
        this.corporateOverview = corporateOverview;
    }

    public String getCorporateBenefits() {
        return corporateBenefits;
    }

    public Corporate corporateBenefits(String corporateBenefits) {
        this.corporateBenefits = corporateBenefits;
        return this;
    }

    public void setCorporateBenefits(String corporateBenefits) {
        this.corporateBenefits = corporateBenefits;
    }

    public String getCorporateWebsite() {
        return corporateWebsite;
    }

    public Corporate corporateWebsite(String corporateWebsite) {
        this.corporateWebsite = corporateWebsite;
        return this;
    }

    public void setCorporateWebsite(String corporateWebsite) {
        this.corporateWebsite = corporateWebsite;
    }

    public String getCorporateFacebook() {
        return corporateFacebook;
    }

    public Corporate corporateFacebook(String corporateFacebook) {
        this.corporateFacebook = corporateFacebook;
        return this;
    }

    public void setCorporateFacebook(String corporateFacebook) {
        this.corporateFacebook = corporateFacebook;
    }

    public String getCorporateTwitter() {
        return corporateTwitter;
    }

    public Corporate corporateTwitter(String corporateTwitter) {
        this.corporateTwitter = corporateTwitter;
        return this;
    }

    public void setCorporateTwitter(String corporateTwitter) {
        this.corporateTwitter = corporateTwitter;
    }

    public String getCorporateInstagram() {
        return corporateInstagram;
    }

    public Corporate corporateInstagram(String corporateInstagram) {
        this.corporateInstagram = corporateInstagram;
        return this;
    }

    public void setCorporateInstagram(String corporateInstagram) {
        this.corporateInstagram = corporateInstagram;
    }

    public String getCorporateLinkedIn() {
        return corporateLinkedIn;
    }

    public Corporate corporateLinkedIn(String corporateLinkedIn) {
        this.corporateLinkedIn = corporateLinkedIn;
        return this;
    }

    public void setCorporateLinkedIn(String corporateLinkedIn) {
        this.corporateLinkedIn = corporateLinkedIn;
    }

    public String getCorporateCulture() {
        return corporateCulture;
    }

    public Corporate corporateCulture(String corporateCulture) {
        this.corporateCulture = corporateCulture;
        return this;
    }

    public void setCorporateCulture(String corporateCulture) {
        this.corporateCulture = corporateCulture;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public Corporate contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCorporatePhone() {
        return corporatePhone;
    }

    public Corporate corporatePhone(String corporatePhone) {
        this.corporatePhone = corporatePhone;
        return this;
    }

    public void setCorporatePhone(String corporatePhone) {
        this.corporatePhone = corporatePhone;
    }

    public String getCorporatePhoneCode() {
        return corporatePhoneCode;
    }

    public Corporate corporatePhoneCode(String corporatePhoneCode) {
        this.corporatePhoneCode = corporatePhoneCode;
        return this;
    }

    public void setCorporatePhoneCode(String corporatePhoneCode) {
        this.corporatePhoneCode = corporatePhoneCode;
    }

    public String getContactPersonDesignation() {
        return contactPersonDesignation;
    }

    public Corporate contactPersonDesignation(String contactPersonDesignation) {
        this.contactPersonDesignation = contactPersonDesignation;
        return this;
    }

    public void setContactPersonDesignation(String contactPersonDesignation) {
        this.contactPersonDesignation = contactPersonDesignation;
    }

    public String getCorporateTagLine() {
        return corporateTagLine;
    }

    public Corporate corporateTagLine(String corporateTagLine) {
        this.corporateTagLine = corporateTagLine;
        return this;
    }

    public void setCorporateTagLine(String corporateTagLine) {
        this.corporateTagLine = corporateTagLine;
    }

    public Country getCountry() {
        return country;
    }

    public Corporate country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Industry getIndustry() {
        return industry;
    }

    public Corporate industry(Industry industry) {
        this.industry = industry;
        return this;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public User getLogin() {
        return login;
    }

    public Corporate login(User user) {
        this.login = user;
        return this;
    }

    public void setLogin(User user) {
        this.login = user;
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
        Corporate corporate = (Corporate) o;
        if (corporate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corporate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Corporate{" +
            "id=" + getId() +
            ", corporateName='" + getCorporateName() + "'" +
            ", corporateAddress='" + getCorporateAddress() + "'" +
            ", corporateCity='" + getCorporateCity() + "'" +
            ", establishedSince='" + getEstablishedSince() + "'" +
            ", corporateEmail='" + getCorporateEmail() + "'" +
            ", corporateOverview='" + getCorporateOverview() + "'" +
            ", corporateBenefits='" + getCorporateBenefits() + "'" +
            ", corporateWebsite='" + getCorporateWebsite() + "'" +
            ", corporateFacebook='" + getCorporateFacebook() + "'" +
            ", corporateTwitter='" + getCorporateTwitter() + "'" +
            ", corporateInstagram='" + getCorporateInstagram() + "'" +
            ", corporateLinkedIn='" + getCorporateLinkedIn() + "'" +
            ", corporateCulture='" + getCorporateCulture() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", corporatePhone='" + getCorporatePhone() + "'" +
            ", corporatePhoneCode='" + getCorporatePhoneCode() + "'" +
            ", contactPersonDesignation='" + getContactPersonDesignation() + "'" +
            ", corporateTagLine='" + getCorporateTagLine() + "'" +
            "}";
    }
}
