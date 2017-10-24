import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FroalaEditorModule } from 'angular-froala-wysiwyg';
import { GradzcircleSharedModule } from '../../shared';

import {
    CandidateProjectService,
    CandidateProjectPopupService,
    CandidateProjectComponent,
    CandidateProjectDetailComponent,
    CandidateProjectDialogComponent,
    CandidateProjectPopupComponent,
    CandidateProjectDeletePopupComponent,
    CandidateProjectDeleteDialogComponent,
    candidateProjectRoute,
    candidateProjectPopupRoute,
    CandidateEducationProjectPopupComponent,
    CandidateEmploymentProjectPopupComponent,
    CandidateEducationProjectPopupService,
    CandidateEmploymentProjectPopupService

} from './';

const ENTITY_STATES = [
    ...candidateProjectRoute,
    ...candidateProjectPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        FroalaEditorModule.forRoot()
    ],
    declarations: [
        CandidateProjectComponent,
        CandidateProjectDetailComponent,
        CandidateProjectDialogComponent,
        CandidateProjectDeleteDialogComponent,
        CandidateProjectPopupComponent,
        CandidateProjectDeletePopupComponent,
        CandidateEducationProjectPopupComponent,
        CandidateEmploymentProjectPopupComponent

    ],
    entryComponents: [
        CandidateProjectComponent,
        CandidateProjectDialogComponent,
        CandidateProjectPopupComponent,
        CandidateProjectDeleteDialogComponent,
        CandidateProjectDeletePopupComponent,
        CandidateEducationProjectPopupComponent,
        CandidateEmploymentProjectPopupComponent

    ],
    providers: [
        CandidateProjectService,
        CandidateProjectPopupService,
        CandidateEmploymentProjectPopupService,
        CandidateEducationProjectPopupService

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateProjectModule {}
