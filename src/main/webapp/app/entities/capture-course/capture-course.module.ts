import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CaptureCourseService,
    CaptureCoursePopupService,
    CaptureCourseComponent,
    CaptureCourseDetailComponent,
    CaptureCourseDialogComponent,
    CaptureCoursePopupComponent,
    CaptureCourseDeletePopupComponent,
    CaptureCourseDeleteDialogComponent,
    captureCourseRoute,
    captureCoursePopupRoute,
} from './';

const ENTITY_STATES = [
    ...captureCourseRoute,
    ...captureCoursePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CaptureCourseComponent,
        CaptureCourseDetailComponent,
        CaptureCourseDialogComponent,
        CaptureCourseDeleteDialogComponent,
        CaptureCoursePopupComponent,
        CaptureCourseDeletePopupComponent,
    ],
    entryComponents: [
        CaptureCourseComponent,
        CaptureCourseDialogComponent,
        CaptureCoursePopupComponent,
        CaptureCourseDeleteDialogComponent,
        CaptureCourseDeletePopupComponent,
    ],
    providers: [
        CaptureCourseService,
        CaptureCoursePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCaptureCourseModule {}
