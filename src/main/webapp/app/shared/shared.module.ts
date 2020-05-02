import { JhiLanguageHelper } from '../core/language/language.helper';
import { JobListEmitterService } from '../entities/job/job-list-change.service';
import { JobPopupService } from '../entities/job/job-popup.service';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { TagInputModule } from 'ngx-chips';
import { GradzcircleSharedLibsModule, GradzcircleSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import {
    CandidatePublicProfilePopupComponent,
    CandidatePublicProfilePopupDialogComponent
} from './candidate-public-profile/candidate-public-profile-popup.component';
import { CandidatePublicProfilePopupService } from './candidate-public-profile/candidate-public-profile-popup.service';
import { FutureDateValidatorDirective } from './helper/future-date-validator.directive';
import { DataStorageService } from './helper/localstorage.service';
import { JobViewForCandidateComponent, JobViewPopupForCandidateComponent } from './job-common/job-view-candidate.component';
import { ProfilePicMgmtPopupComponent, ProfilePicMgmtPopupDialogComponent } from './profile-pic/profile-pic-mgmt-popup.component';
import { ProfileMgmtPopupService } from './profile-pic/profile-pic-mgmt-popup.service';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { CKEditorModule } from 'ngx-ckeditor';
import { DatePipe } from '@angular/common';
import { ArchwizardModule } from 'angular-archwizard';
import { JhiLanguageService } from 'ng-jhipster';
import { ImageCropperModule } from 'ngx-image-cropper';
import { FileUploadModule } from 'ng2-file-upload';

@NgModule({
    imports: [
        GradzcircleSharedLibsModule,
        GradzcircleSharedCommonModule,
        ArchwizardModule,
        MultiselectDropdownModule,
        TagInputModule,
        CKEditorModule,
        FileUploadModule,
        ImageCropperModule
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        FutureDateValidatorDirective,
        ProfilePicMgmtPopupComponent,
        ProfilePicMgmtPopupDialogComponent,
        CandidatePublicProfilePopupComponent,
        CandidatePublicProfilePopupDialogComponent,
        JobViewForCandidateComponent,
        JobViewPopupForCandidateComponent
    ],
    providers: [
        { provide: NgbDateAdapter, useClass: NgbDateMomentAdapter },
        DataStorageService,
        ProfileMgmtPopupService,
        CandidatePublicProfilePopupService,
        JobPopupService,
        JobListEmitterService
    ],
    entryComponents: [
        JhiLoginModalComponent,
        ProfilePicMgmtPopupComponent,
        ProfilePicMgmtPopupDialogComponent,
        CandidatePublicProfilePopupComponent,
        CandidatePublicProfilePopupDialogComponent,
        JobViewForCandidateComponent,
        JobViewPopupForCandidateComponent
    ],
    exports: [
        GradzcircleSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        TagInputModule,
        CKEditorModule,
        FutureDateValidatorDirective,
        ArchwizardModule,
        ImageCropperModule,
        ProfilePicMgmtPopupComponent,
        ProfilePicMgmtPopupDialogComponent,
        FileUploadModule,
        JobViewForCandidateComponent,
        JobViewPopupForCandidateComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleSharedModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
