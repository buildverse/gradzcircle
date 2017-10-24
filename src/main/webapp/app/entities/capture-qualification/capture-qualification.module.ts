import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CaptureQualificationService,
    CaptureQualificationPopupService,
    CaptureQualificationComponent,
    CaptureQualificationDetailComponent,
    CaptureQualificationDialogComponent,
    CaptureQualificationPopupComponent,
    CaptureQualificationDeletePopupComponent,
    CaptureQualificationDeleteDialogComponent,
    captureQualificationRoute,
    captureQualificationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...captureQualificationRoute,
    ...captureQualificationPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CaptureQualificationComponent,
        CaptureQualificationDetailComponent,
        CaptureQualificationDialogComponent,
        CaptureQualificationDeleteDialogComponent,
        CaptureQualificationPopupComponent,
        CaptureQualificationDeletePopupComponent,
    ],
    entryComponents: [
        CaptureQualificationComponent,
        CaptureQualificationDialogComponent,
        CaptureQualificationPopupComponent,
        CaptureQualificationDeleteDialogComponent,
        CaptureQualificationDeletePopupComponent,
    ],
    providers: [
        CaptureQualificationService,
        CaptureQualificationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCaptureQualificationModule {}
