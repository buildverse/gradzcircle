import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {NgbProgressbarConfig} from '@ng-bootstrap/ng-bootstrap';
import {GradzcircleSharedModule} from '../../shared';
import {GradzcircleAdminModule} from '../../admin/admin.module';
import {GradzcircleCandidateLanguageProficiencyModule} from '../../entities/candidate-language-proficiency/candidate-language-proficiency.module';
import {candidateProfileRoutes, candidateProfilePopupRoute, JobResolvePagingParams} from './candidate-profile.route';
import {CandidateProfileAboutMeEditComponent} from './candidate-about-me-edit.component';
import {CandidateProfileComponent} from './candidate-profile.component';
import {CandidateProfileScoreService} from './candidate-profile-score.service';
import {CandidateResolverService} from './candidate-profile-account-resolver.service';
import {CandidateCareerInterestResolverService} from './candidate-profile-career-interest-resolver.service';
import {ProfileHelperService} from '../profile-helper.service';
import {CandidateProfileAboutMeDetailsComponent} from './candidate-about-me-details.component';
import {CandidateMaritalStatusResolverService} from './candidate-profile-marital-status-resolver.service';
import {CountryResolverService} from './candidate-profile-country-resolver.service';
import {CandidateGenderResolverService} from './candidate-profile-gender-resolver.service';
import {FileUploadModule} from 'ng2-file-upload'
import {MultiselectDropdownModule} from 'angular-2-dropdown-multiselect';
import {CandidateVisaResolverService} from './candidate-profile-visa-resolver.service';
import {CandidateLanguageResolverService} from './candidate-profile-language-resolver.service';
import {CandidateAboutMeEditGuard} from './candidate-profile-about-me-edit-guard';
import {CandidateNationalityResolverService} from './candidate-profile-nationality-resolver.service';
import {CandidateDetailResolverService} from './candidate-detail-resolver.service';
import {CandidateLanguageProficiencyDetailsComponent} from './candidate-about-me-language-details.component';
import {CandidateLanguageProficiencyResolverService} from './candidate-language-proficiency-resolver.service';
import {SafeHtml} from './image.pipe';
import {GradzcircleCandidateEducationModule} from '../../entities/candidate-education/candidate-education.module';
import { DataService } from '../../shared/helper/data.service';
import {CandidatePublicProfilePopupComponent, CandidatePublicProfilePopupDialogComponent} from './candidate-public-profile-popup.component';
import {CandidatePublicProfilePopupService} from './candidate-public-profile-popup.service';
import { CandidateProfileMgmtPopupService } from './candidate-profile-pic-mgmt-popup.service';
import {CandidateProfilePicMgmtPopupDialogComponent,CandidateProfilePicMgmtPopupComponent} from './candidate-profile-pic-mgmt-popup.component';
import {AppliedJobsComponent} from './applied-job-by-candidate.component';
import {ShortListedJobsForCandidateComponent} from './shortlisted-for-job.component';
import { ImageCropperModule } from 'ngx-image-cropper';


const ENTITY_STATES = [
  ...candidateProfileRoutes,
  ...candidateProfilePopupRoute
];

@NgModule({
  imports: [
    GradzcircleSharedModule,
    GradzcircleAdminModule,
    MultiselectDropdownModule,
    FileUploadModule,
    GradzcircleCandidateLanguageProficiencyModule,
    GradzcircleCandidateEducationModule,
    ImageCropperModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    CandidateProfileComponent,
    CandidateProfileAboutMeEditComponent,
    CandidateProfileAboutMeDetailsComponent,
    CandidateLanguageProficiencyDetailsComponent,
    SafeHtml,
    CandidatePublicProfilePopupComponent,
    CandidatePublicProfilePopupDialogComponent,
    AppliedJobsComponent,
    ShortListedJobsForCandidateComponent,
    CandidateProfilePicMgmtPopupComponent,
    CandidateProfilePicMgmtPopupDialogComponent

  ],
  providers: [
    ProfileHelperService,
    CandidateResolverService,
    CandidateCareerInterestResolverService,
    CandidateMaritalStatusResolverService,
    CountryResolverService,
    CandidateGenderResolverService,
    CandidateVisaResolverService,
    CandidateLanguageResolverService,
    CandidateAboutMeEditGuard,
    CandidateNationalityResolverService,
    CandidateDetailResolverService,
    CandidateLanguageProficiencyResolverService,
    CandidatePublicProfilePopupService,
    JobResolvePagingParams,
    DataService,
    NgbProgressbarConfig,
    CandidateProfileScoreService,
    CandidateProfileMgmtPopupService

  ],
  entryComponents: [
    CandidatePublicProfilePopupComponent,
    CandidatePublicProfilePopupDialogComponent,
    CandidateProfilePicMgmtPopupComponent,
    CandidateProfilePicMgmtPopupDialogComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class CandidateProfileModule {}
