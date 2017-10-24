import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FroalaEditorModule } from 'angular-froala-wysiwyg';
import { GradzcircleSharedModule } from '../../shared';
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
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        FroalaEditorModule.forRoot()
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
        CandidateCertificationPopupServiceNew
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateCertificationModule {}
