import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GradzcircleCandidateModule } from './candidate/candidate.module';
import { GradzcircleAddressModule } from './address/address.module';
import { GradzcircleCandidateCertificationModule } from './candidate-certification/candidate-certification.module';
import { GradzcircleCandidateEducationModule } from './candidate-education/candidate-education.module';
import { GradzcircleCandidateEmploymentModule } from './candidate-employment/candidate-employment.module';
import { GradzcircleCandidateNonAcademicWorkModule } from './candidate-non-academic-work/candidate-non-academic-work.module';
import { GradzcircleCandidateProjectModule } from './candidate-project/candidate-project.module';
import { GradzcircleCandidateLanguageProficiencyModule } from './candidate-language-proficiency/candidate-language-proficiency.module';
import { GradzcircleVisaTypeModule } from './visa-type/visa-type.module';
import { GradzcircleMaritalStatusModule } from './marital-status/marital-status.module';
import { GradzcircleEmploymentTypeModule } from './employment-type/employment-type.module';
import { GradzcircleQualificationModule } from './qualification/qualification.module';
import { GradzcircleGenderModule } from './gender/gender.module';
import { GradzcircleCourseModule } from './course/course.module';
import { GradzcircleCountryModule } from './country/country.module';
import { GradzcircleNationalityModule } from './nationality/nationality.module';
import { GradzcircleIndustryModule } from './industry/industry.module';
import { GradzcircleSkillsModule } from './skills/skills.module';
import { GradzcircleJobTypeModule } from './job-type/job-type.module';
import { GradzcircleCollegeModule } from './college/college.module';
import { GradzcircleUniversityModule } from './university/university.module';
import { GradzcircleJobCategoryModule } from './job-category/job-category.module';
import { GradzcircleLanguageModule } from './language/language.module';
import { GradzcircleAuditModule } from './audit/audit.module';
import { GradzcircleEmployabilityModule } from './employability/employability.module';
import { GradzcircleCorporateModule } from './corporate/corporate.module';
import { GradzcircleErrorMessagesModule } from './error-messages/error-messages.module';
import { GradzcircleCaptureCourseModule } from './capture-course/capture-course.module';
import { GradzcircleCaptureUniversityModule } from './capture-university/capture-university.module';
import { GradzcircleCaptureCollegeModule } from './capture-college/capture-college.module';
import { GradzcircleCaptureQualificationModule } from './capture-qualification/capture-qualification.module';
import { GradzcircleJobModule } from './job/job.module';
import { GradzcircleFilterCategoryModule } from './filter-category/filter-category.module';
import { GradzcircleFilterModule } from './filter/filter.module';
import { GradzcircleJobFilterModule } from './job-filter/job-filter.module';
import { GradzcircleJobHistoryModule } from './job-history/job-history.module';
import { GradzcircleJobFilterHistoryModule } from './job-filter-history/job-filter-history.module';
import { GradzcircleAppConfigModule } from './app-config/app-config.module';
import { GradzcircleStatesModule } from './states/states.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GradzcircleCandidateModule,
        GradzcircleAddressModule,
        GradzcircleCandidateCertificationModule,
        GradzcircleCandidateEducationModule,
        GradzcircleCandidateEmploymentModule,
        GradzcircleCandidateNonAcademicWorkModule,
        GradzcircleCandidateProjectModule,
        GradzcircleCandidateLanguageProficiencyModule,
        GradzcircleVisaTypeModule,
        GradzcircleMaritalStatusModule,
        GradzcircleEmploymentTypeModule,
        GradzcircleQualificationModule,
        GradzcircleGenderModule,
        GradzcircleCourseModule,
        GradzcircleCountryModule,
        GradzcircleNationalityModule,
        GradzcircleIndustryModule,
        GradzcircleSkillsModule,
        GradzcircleJobTypeModule,
        GradzcircleCollegeModule,
        GradzcircleUniversityModule,
        GradzcircleJobCategoryModule,
        GradzcircleLanguageModule,
        GradzcircleAuditModule,
        GradzcircleEmployabilityModule,
        GradzcircleCorporateModule,
        GradzcircleErrorMessagesModule,
        GradzcircleCaptureCourseModule,
        GradzcircleCaptureUniversityModule,
        GradzcircleCaptureCollegeModule,
        GradzcircleCaptureQualificationModule,
        GradzcircleJobModule,
        GradzcircleFilterCategoryModule,
        GradzcircleFilterModule,
        GradzcircleJobFilterModule,
        GradzcircleJobHistoryModule,
        GradzcircleJobFilterHistoryModule,
        GradzcircleAppConfigModule,
        GradzcircleStatesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleEntityModule {}
