import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule } from '../../shared';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { GradzcircleCandidateProfileSharedModule } from '../../profiles/candidate/candidate-profile-shared.module';
import {
    CandidateCertificationService,
    CandidateCertificationPopupService,
    CandidateCertificationDetailComponent,
    //  CandidateCertificationDialogComponent,
    CandidateCertificationPopupComponent,
    CandidateCertificationDeletePopupComponent,
    CandidateCertificationDeleteDialogComponent,
    candidateCertificationRoute,
    candidateCertificationPopupRoute,
    CandidateCertification,
    //   CandidateCertificationPopupNewComponent,
    CandidateCertificationPopupServiceNew
} from './';

const ENTITY_STATES = [...candidateCertificationRoute, ...candidateCertificationPopupRoute];

@NgModule({
    imports: [GradzcircleSharedModule, GradzcircleCandidateProfileSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CandidateCertificationDetailComponent,
        //   CandidateCertificationDialogComponent,
        CandidateCertificationDeleteDialogComponent,
        CandidateCertificationPopupComponent,
        CandidateCertificationDeletePopupComponent
        //    CandidateCertificationPopupNewComponent
    ],
    entryComponents: [
        //    CandidateCertificationDialogComponent,
        CandidateCertificationPopupComponent,
        CandidateCertificationDeleteDialogComponent,
        CandidateCertificationDeletePopupComponent
        //    CandidateCertificationPopupNewComponent
    ],
    providers: [
        CandidateCertificationService,
        CandidateCertificationPopupService,
        CandidateCertificationPopupServiceNew,
        DataStorageService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateCertificationModule {}
