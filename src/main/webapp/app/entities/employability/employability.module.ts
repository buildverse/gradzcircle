import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    EmployabilityService,
    EmployabilityPopupService,
    EmployabilityComponent,
    EmployabilityDetailComponent,
    EmployabilityDialogComponent,
    EmployabilityPopupComponent,
    EmployabilityDeletePopupComponent,
    EmployabilityDeleteDialogComponent,
    employabilityRoute,
    employabilityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...employabilityRoute,
    ...employabilityPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EmployabilityComponent,
        EmployabilityDetailComponent,
        EmployabilityDialogComponent,
        EmployabilityDeleteDialogComponent,
        EmployabilityPopupComponent,
        EmployabilityDeletePopupComponent,
    ],
    entryComponents: [
        EmployabilityComponent,
        EmployabilityDialogComponent,
        EmployabilityPopupComponent,
        EmployabilityDeleteDialogComponent,
        EmployabilityDeletePopupComponent,
    ],
    providers: [
        EmployabilityService,
        EmployabilityPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleEmployabilityModule {}
