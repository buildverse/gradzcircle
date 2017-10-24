import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    UniversityService,
    UniversityPopupService,
    UniversityComponent,
    UniversityDetailComponent,
    UniversityDialogComponent,
    UniversityPopupComponent,
    UniversityDeletePopupComponent,
    UniversityDeleteDialogComponent,
    universityRoute,
    universityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...universityRoute,
    ...universityPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UniversityComponent,
        UniversityDetailComponent,
        UniversityDialogComponent,
        UniversityDeleteDialogComponent,
        UniversityPopupComponent,
        UniversityDeletePopupComponent,
    ],
    entryComponents: [
        UniversityComponent,
        UniversityDialogComponent,
        UniversityPopupComponent,
        UniversityDeleteDialogComponent,
        UniversityDeletePopupComponent,
    ],
    providers: [
        UniversityService,
        UniversityPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleUniversityModule {}
