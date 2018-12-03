import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';


import { GradzcircleSharedModule } from '../../shared';
import {
    CandidateNonAcademicWorkService,
    CandidateNonAcademicWorkPopupService,
    CandidateNonAcademicWorkComponent,
    CandidateNonAcademicWorkDetailComponent,
    CandidateNonAcademicWorkDialogComponent,
    CandidateNonAcademicWorkPopupComponent,
    CandidateNonAcademicWorkDeletePopupComponent,
    CandidateNonAcademicWorkDeleteDialogComponent,
    candidateNonAcademicWorkRoute,
    candidateNonAcademicWorkPopupRoute,
    CandidateNonAcademicWorkPopupComponentNew,
    CandidateNonAcademicWorkPopupServiceNew

} from './';

const ENTITY_STATES = [
    ...candidateNonAcademicWorkRoute,
    ...candidateNonAcademicWorkPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
       RouterModule.forChild(ENTITY_STATES)
      

    ],
    declarations: [
        CandidateNonAcademicWorkComponent,
        CandidateNonAcademicWorkDetailComponent,
        CandidateNonAcademicWorkDialogComponent,
        CandidateNonAcademicWorkDeleteDialogComponent,
        CandidateNonAcademicWorkPopupComponent,
        CandidateNonAcademicWorkDeletePopupComponent,
        CandidateNonAcademicWorkPopupComponentNew

    ],
    entryComponents: [
        CandidateNonAcademicWorkComponent,
        CandidateNonAcademicWorkDialogComponent,
        CandidateNonAcademicWorkPopupComponent,
        CandidateNonAcademicWorkDeleteDialogComponent,
        CandidateNonAcademicWorkDeletePopupComponent,
        CandidateNonAcademicWorkPopupComponentNew

    ],
    providers: [
        CandidateNonAcademicWorkService,
        CandidateNonAcademicWorkPopupService,
        CandidateNonAcademicWorkPopupServiceNew

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateNonAcademicWorkModule {}
