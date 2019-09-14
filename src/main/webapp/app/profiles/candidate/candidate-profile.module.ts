import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {NgbProgressbarConfig} from '@ng-bootstrap/ng-bootstrap';
import {GradzcircleSharedModule} from '../../shared';
import {candidateProfileRoutes, candidateProfilePopupRoute, JobResolvePagingParams} from './candidate-profile.route';
import {CandidateProfileComponent} from './candidate-profile-settings.component';
import {CandidateProfileSettingService} from './candidate-profile-setting.service';
import {CandidateResolverService} from './candidate-profile-account-resolver.service';
import {CandidateCareerInterestResolverService} from './candidate-profile-career-interest-resolver.service';
import {ProfileHelperService} from '../profile-helper.service';
// import {CandidateMaritalStatusResolverService} from './candidate-profile-marital-status-resolver.service';
import {CandidateGenderResolverService} from './candidate-profile-gender-resolver.service';
import {FileUploadModule} from 'ng2-file-upload'
import {MultiselectDropdownModule} from 'angular-2-dropdown-multiselect';
// import {CandidateAboutMeEditGuard} from './candidate-profile-about-me-edit-guard';
import {CandidateDetailResolverService} from './candidate-detail-resolver.service';
// import {CandidateLanguageProficiencyDetailsComponent} from './candidate-about-me-language-details.component';
//import {SafeHtml} from './image.pipe';
import {DataStorageService} from '../../shared/helper/localstorage.service';
import {CandidatePublicProfilePopupComponent, CandidatePublicProfilePopupDialogComponent} from './candidate-public-profile-popup.component';
import {CandidatePublicProfilePopupService} from './candidate-public-profile-popup.service';
import {ProfileMgmtPopupService} from './profile-pic-mgmt-popup.service';
import {ProfilePicMgmtPopupDialogComponent, ProfilePicMgmtPopupComponent} from './profile-pic-mgmt-popup.component';
import {AppliedJobsComponent} from './applied-job-by-candidate.component';
import {ShortListedJobsForCandidateComponent} from './shortlisted-for-job.component';
import {ImageCropperModule} from 'ngx-image-cropper';
import {CandidatePrimarySettingsEditComponent} from './candidate-primary-settings-edit.component';
import {CandidateProfilePrimaryViewComponent} from './candidate-primary-settings-view.component';
import { CandidateProfileContactViewComponent} from './candidate-profile-contact-setting-view.component';
import {CandidateContactSettingsEditComponent} from './candidate-profile-contact-settings-edit.component';

const ENTITY_STATES = [
  ...candidateProfileRoutes,
  ...candidateProfilePopupRoute
];

@NgModule({
  imports: [
    GradzcircleSharedModule,
    MultiselectDropdownModule,
    FileUploadModule,
    ImageCropperModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    CandidateProfileComponent,
    //CandidateLanguageProficiencyDetailsComponent,
   // SafeHtml,
    CandidatePublicProfilePopupComponent,
    CandidatePublicProfilePopupDialogComponent,
    AppliedJobsComponent,
    ShortListedJobsForCandidateComponent,
    ProfilePicMgmtPopupComponent,
    ProfilePicMgmtPopupDialogComponent,
    CandidatePrimarySettingsEditComponent,
    CandidateProfilePrimaryViewComponent,
    CandidateProfileContactViewComponent,
    CandidateContactSettingsEditComponent
  ],
  providers: [
    ProfileHelperService,
    CandidateResolverService,
    CandidateCareerInterestResolverService,
    //CandidateMaritalStatusResolverService,
    // CountryResolverService,
    CandidateGenderResolverService,
    //  CandidateVisaResolverService,
    //  CandidateLanguageResolverService,
    //CandidateAboutMeEditGuard,
    //  CandidateNationalityResolverService,
    CandidateDetailResolverService,
    // CandidateLanguageProficiencyResolverService,
    CandidatePublicProfilePopupService,
    JobResolvePagingParams,
    DataStorageService,
    NgbProgressbarConfig,
    CandidateProfileSettingService,
    ProfileMgmtPopupService

  ],
  entryComponents: [
    CandidatePublicProfilePopupComponent,
    CandidatePublicProfilePopupDialogComponent,
    ProfilePicMgmtPopupComponent,
    ProfilePicMgmtPopupDialogComponent,
    CandidatePrimarySettingsEditComponent,
    CandidateProfilePrimaryViewComponent,
    CandidateProfileContactViewComponent,
    CandidateContactSettingsEditComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class CandidateProfileModule {}
