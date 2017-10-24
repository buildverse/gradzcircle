import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    SkillsService,
    SkillsPopupService,
    SkillsComponent,
    SkillsDetailComponent,
    SkillsDialogComponent,
    SkillsPopupComponent,
    SkillsDeletePopupComponent,
    SkillsDeleteDialogComponent,
    skillsRoute,
    skillsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...skillsRoute,
    ...skillsPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SkillsComponent,
        SkillsDetailComponent,
        SkillsDialogComponent,
        SkillsDeleteDialogComponent,
        SkillsPopupComponent,
        SkillsDeletePopupComponent,
    ],
    entryComponents: [
        SkillsComponent,
        SkillsDialogComponent,
        SkillsPopupComponent,
        SkillsDeleteDialogComponent,
        SkillsDeletePopupComponent,
    ],
    providers: [
        SkillsService,
        SkillsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleSkillsModule {}
