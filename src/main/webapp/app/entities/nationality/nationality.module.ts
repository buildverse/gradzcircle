import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    NationalityService,
    NationalityPopupService,
    NationalityComponent,
    NationalityDetailComponent,
    NationalityDialogComponent,
    NationalityPopupComponent,
    NationalityDeletePopupComponent,
    NationalityDeleteDialogComponent,
    nationalityRoute,
    nationalityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...nationalityRoute,
    ...nationalityPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        NationalityComponent,
        NationalityDetailComponent,
        NationalityDialogComponent,
        NationalityDeleteDialogComponent,
        NationalityPopupComponent,
        NationalityDeletePopupComponent,
    ],
    entryComponents: [
        NationalityComponent,
        NationalityDialogComponent,
        NationalityPopupComponent,
        NationalityDeleteDialogComponent,
        NationalityDeletePopupComponent,
    ],
    providers: [
        NationalityService,
        NationalityPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleNationalityModule {}
