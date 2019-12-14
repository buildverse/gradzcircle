import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CandidateSkillsService,
    CandidateSkillsPopupService,
    CandidateSkillsComponent,
    CandidateSkillsDetailComponent,
    CandidateSkillsDialogComponent,
    CandidateSkillsPopupComponent,
    CandidateSkillsDeletePopupComponent,
    CandidateSkillsDeleteDialogComponent,
    candidateSkillsRoute,
    candidateSkillsPopupRoute,
    CandidateCurrentSkillsResolverService,
    CandidateSkillsPopupNewComponent,
    CandidateSkillsPopupServiceNew
} from './';

const ENTITY_STATES = [...candidateSkillsRoute, ...candidateSkillsPopupRoute];

@NgModule({
    imports: [GradzcircleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CandidateSkillsComponent,
        CandidateSkillsDetailComponent,
        CandidateSkillsDialogComponent,
        CandidateSkillsDeleteDialogComponent,
        CandidateSkillsPopupComponent,
        CandidateSkillsDeletePopupComponent,
        CandidateSkillsPopupNewComponent
    ],
    entryComponents: [
        CandidateSkillsComponent,
        CandidateSkillsDialogComponent,
        CandidateSkillsPopupComponent,
        CandidateSkillsDeleteDialogComponent,
        CandidateSkillsDeletePopupComponent,
        CandidateSkillsPopupNewComponent
    ],
    providers: [CandidateSkillsService, CandidateSkillsPopupService, CandidateCurrentSkillsResolverService, CandidateSkillsPopupServiceNew],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateSkillsModule {}
