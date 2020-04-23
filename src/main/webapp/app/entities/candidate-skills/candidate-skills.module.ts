import { GradzcircleCandidateProfileSharedModule } from '../../profiles/candidate/candidate-profile-shared.module';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import { SkillsService } from '../skills/skills.service';
import {
    CandidateSkillsService,
    CandidateSkillsPopupService,
    // CandidateSkillsComponent,
    CandidateSkillsDetailComponent,
    //   CandidateSkillsDialogComponent,
    CandidateSkillsPopupComponent,
    CandidateSkillsDeletePopupComponent,
    CandidateSkillsDeleteDialogComponent,
    candidateSkillsRoute,
    candidateSkillsPopupRoute,
    CandidateCurrentSkillsResolverService,
    //  CandidateSkillsPopupNewComponent,
    CandidateSkillsPopupServiceNew
} from './';

const ENTITY_STATES = [...candidateSkillsRoute, ...candidateSkillsPopupRoute];

@NgModule({
    imports: [GradzcircleSharedModule, RouterModule.forChild(ENTITY_STATES), GradzcircleCandidateProfileSharedModule],
    declarations: [
        // CandidateSkillsComponent,
        CandidateSkillsDetailComponent,
        //  CandidateSkillsDialogComponent,
        CandidateSkillsDeleteDialogComponent,
        CandidateSkillsPopupComponent,
        CandidateSkillsDeletePopupComponent
        //     CandidateSkillsPopupNewComponent
    ],
    entryComponents: [
        //  CandidateSkillsComponent,
        //    CandidateSkillsDialogComponent,
        CandidateSkillsPopupComponent,
        CandidateSkillsDeleteDialogComponent,
        CandidateSkillsDeletePopupComponent
        //   CandidateSkillsPopupNewComponent
    ],
    providers: [
        CandidateSkillsService,
        CandidateSkillsPopupService,
        CandidateCurrentSkillsResolverService,
        CandidateSkillsPopupServiceNew,
        SkillsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateSkillsModule {}
