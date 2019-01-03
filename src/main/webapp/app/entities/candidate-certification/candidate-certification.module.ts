import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule, DataService } from '../../shared';
import {
    CandidateCertificationService,
    CandidateCertificationPopupService,
    CandidateCertificationComponent,
    CandidateCertificationDetailComponent,
    CandidateCertificationDialogComponent,
    CandidateCertificationPopupComponent,
    CandidateCertificationDeletePopupComponent,
    CandidateCertificationDeleteDialogComponent,
    candidateCertificationRoute,
    candidateCertificationPopupRoute,
    CandidateCertification,
    CandidateCertificationPopupComponentNew,
    CandidateCertificationPopupServiceNew
} from './';

const ENTITY_STATES = [
    ...candidateCertificationRoute,
    ...candidateCertificationPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateCertificationComponent,
        CandidateCertificationDetailComponent,
        CandidateCertificationDialogComponent,
        CandidateCertificationDeleteDialogComponent,
        CandidateCertificationPopupComponent,
        CandidateCertificationDeletePopupComponent,
        CandidateCertificationPopupComponentNew
    ],
    entryComponents: [
        CandidateCertificationComponent,
        CandidateCertificationDialogComponent,
        CandidateCertificationPopupComponent,
        CandidateCertificationDeleteDialogComponent,
        CandidateCertificationDeletePopupComponent,
        CandidateCertificationPopupComponentNew
    ],
    providers: [
        CandidateCertificationService,
        CandidateCertificationPopupService,
        CandidateCertificationPopupServiceNew,
        DataService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateCertificationModule {}
