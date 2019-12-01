
package com.drishika.gradzcircle.config;


import java.time.Duration;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.config.builders.*;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
            	    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.drishika.gradzcircle.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Audit.class.getName(), jcacheConfiguration);
            /* cm.createCache(com.drishika.gradzcircle.domain.EmploymentType.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobType.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".addresses", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".educations", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".nonAcademics", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".certifications", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".employments", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".candidateLanguageProficiencies", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".jobCategories", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".jobs", jcacheConfiguration);
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
            cm.createCache(com.drishika.gradzcircle.domain.Corporate.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.ErrorMessages.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureCourse.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureUniversity.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureCollege.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CaptureQualification.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName() + ".jobFilters", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName() + ".histories", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName() + ".candidates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Filter.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobFilter.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobFilter.class.getName() + ".histories", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.JobFilterHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.AppConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateJob.class.getName(), jcacheConfiguration);
			cm.createCache(com.drishika.gradzcircle.domain.CandidateAppliedJobs.class.getName(), jcacheConfiguration);
			cm.createCache(com.drishika.gradzcircle.domain.CorporateCandidate.class.getName(), jcacheConfiguration);
			cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".appliedJobs",jcacheConfiguration);
			cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName() + ".appliedCandidates",jcacheConfiguration);
			cm.createCache(com.drishika.gradzcircle.domain.Job.class.getName() + ".candidateJobs", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.States.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.ProfileCategory.class.getName(), jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".profileCategories", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.ProfileCategory.class.getName() + ".profileScores", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateProfileScore.class.getName(), jcacheConfiguration);*/
           /* cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".corporates", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Corporate.class.getName() + ".candidates", jcacheConfiguration);
             cm.createCache(com.drishika.gradzcircle.domain.Candidate.class.getName() + ".candidateSkills", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.Skills.class.getName() + ".candidateSkills", jcacheConfiguration);
            cm.createCache(com.drishika.gradzcircle.domain.CandidateSkills.class.getName(), jcacheConfiguration);*/
            // jhipster-needle-ehcache-add-entry
        };
    }
}
