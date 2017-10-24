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
} from './';

const ENTITY_STATES = [
    ...candidateRoute,
    ...candidatePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CandidateComponent,
        CandidateDetailComponent,
        CandidateDialogComponent,
        CandidateDeleteDialogComponent,
        CandidatePopupComponent,
        CandidateDeletePopupComponent,
    ],
    entryComponents: [
        CandidateComponent,
        CandidateDialogComponent,
        CandidatePopupComponent,
        CandidateDeleteDialogComponent,
        CandidateDeletePopupComponent,
    ],
    providers: [
        CandidateService,
        CandidatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateModule {}
