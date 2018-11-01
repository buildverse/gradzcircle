import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CollegeService,
    CollegePopupService,
    CollegeComponent,
    CollegeDetailComponent,
    CollegeDialogComponent,
    CollegePopupComponent,
    CollegeDeletePopupComponent,
    CollegeDeleteDialogComponent,
    collegeRoute,
    collegePopupRoute,
} from './';

const ENTITY_STATES = [
    ...collegeRoute,
    ...collegePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CollegeComponent,
        CollegeDetailComponent,
        CollegeDialogComponent,
        CollegeDeleteDialogComponent,
        CollegePopupComponent,
        CollegeDeletePopupComponent,
    ],
    entryComponents: [
        CollegeComponent,
        CollegeDialogComponent,
        CollegePopupComponent,
        CollegeDeleteDialogComponent,
        CollegeDeletePopupComponent,
    ],
    providers: [
        CollegeService,
        CollegePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCollegeModule {}
