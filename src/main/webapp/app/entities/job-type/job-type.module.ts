import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    JobTypeService,
    JobTypePopupService,
    JobTypeComponent,
    JobTypeDetailComponent,
    JobTypeDialogComponent,
    JobTypePopupComponent,
    JobTypeDeletePopupComponent,
    JobTypeDeleteDialogComponent,
    jobTypeRoute,
    jobTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobTypeRoute,
    ...jobTypePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        JobTypeComponent,
        JobTypeDetailComponent,
        JobTypeDialogComponent,
        JobTypeDeleteDialogComponent,
        JobTypePopupComponent,
        JobTypeDeletePopupComponent,
    ],
    entryComponents: [
        JobTypeComponent,
        JobTypeDialogComponent,
        JobTypePopupComponent,
        JobTypeDeleteDialogComponent,
        JobTypeDeletePopupComponent,
    ],
    providers: [
        JobTypeService,
        JobTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobTypeModule {}
