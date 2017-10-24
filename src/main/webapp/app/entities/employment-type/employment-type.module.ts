import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    EmploymentTypeService,
    EmploymentTypePopupService,
    EmploymentTypeComponent,
    EmploymentTypeDetailComponent,
    EmploymentTypeDialogComponent,
    EmploymentTypePopupComponent,
    EmploymentTypeDeletePopupComponent,
    EmploymentTypeDeleteDialogComponent,
    employmentTypeRoute,
    employmentTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...employmentTypeRoute,
    ...employmentTypePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmploymentTypeComponent,
        EmploymentTypeDetailComponent,
        EmploymentTypeDialogComponent,
        EmploymentTypeDeleteDialogComponent,
        EmploymentTypePopupComponent,
        EmploymentTypeDeletePopupComponent,
    ],
    entryComponents: [
        EmploymentTypeComponent,
        EmploymentTypeDialogComponent,
        EmploymentTypePopupComponent,
        EmploymentTypeDeleteDialogComponent,
        EmploymentTypeDeletePopupComponent,
    ],
    providers: [
        EmploymentTypeService,
        EmploymentTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleEmploymentTypeModule {}
