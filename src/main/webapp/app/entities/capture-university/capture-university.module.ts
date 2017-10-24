import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CaptureUniversityService,
    CaptureUniversityPopupService,
    CaptureUniversityComponent,
    CaptureUniversityDetailComponent,
    CaptureUniversityDialogComponent,
    CaptureUniversityPopupComponent,
    CaptureUniversityDeletePopupComponent,
    CaptureUniversityDeleteDialogComponent,
    captureUniversityRoute,
    captureUniversityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...captureUniversityRoute,
    ...captureUniversityPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CaptureUniversityComponent,
        CaptureUniversityDetailComponent,
        CaptureUniversityDialogComponent,
        CaptureUniversityDeleteDialogComponent,
        CaptureUniversityPopupComponent,
        CaptureUniversityDeletePopupComponent,
    ],
    entryComponents: [
        CaptureUniversityComponent,
        CaptureUniversityDialogComponent,
        CaptureUniversityPopupComponent,
        CaptureUniversityDeleteDialogComponent,
        CaptureUniversityDeletePopupComponent,
    ],
    providers: [
        CaptureUniversityService,
        CaptureUniversityPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCaptureUniversityModule {}
