import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    CaptureCollegeService,
    CaptureCollegePopupService,
    CaptureCollegeComponent,
    CaptureCollegeDetailComponent,
    CaptureCollegeDialogComponent,
    CaptureCollegePopupComponent,
    CaptureCollegeDeletePopupComponent,
    CaptureCollegeDeleteDialogComponent,
    captureCollegeRoute,
    captureCollegePopupRoute,
} from './';

const ENTITY_STATES = [
    ...captureCollegeRoute,
    ...captureCollegePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CaptureCollegeComponent,
        CaptureCollegeDetailComponent,
        CaptureCollegeDialogComponent,
        CaptureCollegeDeleteDialogComponent,
        CaptureCollegePopupComponent,
        CaptureCollegeDeletePopupComponent,
    ],
    entryComponents: [
        CaptureCollegeComponent,
        CaptureCollegeDialogComponent,
        CaptureCollegePopupComponent,
        CaptureCollegeDeleteDialogComponent,
        CaptureCollegeDeletePopupComponent,
    ],
    providers: [
        CaptureCollegeService,
        CaptureCollegePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCaptureCollegeModule {}
