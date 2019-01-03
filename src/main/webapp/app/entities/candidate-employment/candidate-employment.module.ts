import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule, DataService } from '../../shared';
import {
    CandidateEmploymentService,
    CandidateEmploymentPopupService,
    CandidateEmploymentComponent,
    CandidateEmploymentDetailComponent,
    CandidateEmploymentDialogComponent,
    CandidateEmploymentPopupComponent,
    CandidateEmploymentDeletePopupComponent,
    CandidateEmploymentDeleteDialogComponent,
    candidateEmploymentRoute,
    candidateEmploymentPopupRoute,
    CandidateEmploymentPopupComponentNew,
    CandidateEmploymentPopupServiceNew

} from './';

const ENTITY_STATES = [
    ...candidateEmploymentRoute,
    ...candidateEmploymentPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)

    ],
    declarations: [
        CandidateEmploymentComponent,
        CandidateEmploymentDetailComponent,
        CandidateEmploymentDialogComponent,
        CandidateEmploymentDeleteDialogComponent,
        CandidateEmploymentPopupComponent,
        CandidateEmploymentDeletePopupComponent,
        CandidateEmploymentPopupComponentNew

    ],
    entryComponents: [
        CandidateEmploymentComponent,
        CandidateEmploymentDialogComponent,
        CandidateEmploymentPopupComponent,
        CandidateEmploymentDeleteDialogComponent,
        CandidateEmploymentDeletePopupComponent,
        CandidateEmploymentPopupComponentNew

    ],
    providers: [
        CandidateEmploymentService,
        CandidateEmploymentPopupService,
        CandidateEmploymentPopupServiceNew,
        DataService

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateEmploymentModule { }
