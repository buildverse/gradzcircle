import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbProgressbarConfig } from '@ng-bootstrap/ng-bootstrap';
import { GradzcircleSharedModule } from '../../shared';
import { candidateProfileRoutes, candidateProfilePopupRoute, JobResolvePagingParams } from './candidate-profile.route';
import { CandidateProfileComponent } from './candidate-profile-settings.component';
import { CandidateProfileSettingService } from './candidate-profile-setting.service';
import { CandidateResolverService } from './candidate-profile-account-resolver.service';
import { JobCategoryService } from '../../entities/job-category/job-category.service';
import { GenderService } from '../../entities/gender/gender.service';
import { NationalityService } from '../../entities/nationality/nationality.service';
import { CandidateCareerInterestResolverService } from './candidate-profile-career-interest-resolver.service';
import { ProfileHelperService } from '../profile-helper.service';
import { CandidateGenderResolverService } from './candidate-profile-gender-resolver.service';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { CandidateDetailResolverService } from './candidate-detail-resolver.service';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { AppliedJobsComponent } from './applied-job-by-candidate.component';
import { ShortListedJobsForCandidateComponent } from './shortlisted-for-job.component';
import { CandidatePrimarySettingsEditComponent } from './candidate-primary-settings-edit.component';
import { CandidateProfilePrimaryViewComponent } from './candidate-primary-settings-view.component';
import { CandidateProfileContactViewComponent } from './candidate-profile-contact-setting-view.component';
import { CandidateContactSettingsEditComponent } from './candidate-profile-contact-settings-edit.component';
import { GradzcircleCandidateProfileSharedModule } from './candidate-profile-shared.module';

const ENTITY_STATES = [...candidateProfileRoutes, ...candidateProfilePopupRoute];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleCandidateProfileSharedModule,
        MultiselectDropdownModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateProfileComponent,
        AppliedJobsComponent,
        ShortListedJobsForCandidateComponent,
        CandidatePrimarySettingsEditComponent,
        CandidateProfilePrimaryViewComponent,
        CandidateProfileContactViewComponent,
        CandidateContactSettingsEditComponent
    ],
    providers: [
        ProfileHelperService,
        CandidateResolverService,
        CandidateCareerInterestResolverService,
        CandidateGenderResolverService,
        CandidateDetailResolverService,
        JobResolvePagingParams,
        DataStorageService,
        NgbProgressbarConfig,
        CandidateProfileSettingService,
        JobCategoryService,
        NationalityService
    ],
    entryComponents: [
        CandidatePrimarySettingsEditComponent,
        CandidateProfilePrimaryViewComponent,
        CandidateProfileContactViewComponent,
        CandidateContactSettingsEditComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CandidateProfileModule {}
