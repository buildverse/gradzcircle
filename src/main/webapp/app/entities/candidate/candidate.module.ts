import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import { GradzcircleAdminModule } from '../../admin/admin.module';
import {
    CandidateService,
    CandidatePopupService,
    CandidateComponent,
    CandidateDetailComponent,
    CandidateDialogComponent,
    CandidatePopupComponent,
    CandidateDeletePopupComponent,
    CandidateDeleteDialogComponent,
    candidateRoute,
    candidatePopupRoute,
    CandidatePagingParams,
    CandidatePreviewComponent
} from './';

const ENTITY_STATES = [
    ...candidateRoute,
    ...candidatePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateComponent,
        CandidateDetailComponent,
        CandidateDialogComponent,
        CandidateDeleteDialogComponent,
        CandidatePopupComponent,
        CandidateDeletePopupComponent,
        CandidatePreviewComponent
    ],
    entryComponents: [
        CandidateComponent,
        CandidateDialogComponent,
        CandidatePopupComponent,
        CandidateDeleteDialogComponent,
        CandidateDeletePopupComponent,
        CandidatePreviewComponent
    ],
    providers: [
        CandidateService,
        CandidatePopupService,
        CandidatePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateModule {}
