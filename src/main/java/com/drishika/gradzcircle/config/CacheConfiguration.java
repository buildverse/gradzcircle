package com.drishika.gradzcircle.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".educations", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".nonAcademics", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".certifications", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".employments", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".candidateLanguageProficiencies", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".jobCategories", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateCertification.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateEducation.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateEducation.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateEmployment.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateEmployment.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateNonAcademicWork.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateProject.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateLanguageProficiency.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.VisaType.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.VisaType.class.getName() + ".candidates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.MaritalStatus.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.EmploymentType.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.EmploymentType.class.getName() + ".candidateEmployments", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Qualification.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Qualification.class.getName() + ".candidateEducations", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Gender.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Course.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Course.class.getName() + ".candidateEducations", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName() + ".addresses", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName() + ".universities", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName() + ".candidateEmployments", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName() + ".visas", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Country.class.getName() + ".corporates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Nationality.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Industry.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Industry.class.getName() + ".corporates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Skills.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobType.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobType.class.getName() + ".candidateEmployments", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.College.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.College.class.getName() + ".candidateEducations", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.University.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.University.class.getName() + ".colleges", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobCategory.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobCategory.class.getName() + ".candidates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Language.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Language.class.getName() + ".candidateLanguageProficiencies", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Audit.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Employability.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Corporate.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.ErrorMessages.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureCourse.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureUniversity.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureCollege.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureQualification.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
