import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    JobFilterService,
    JobFilterPopupService,
    JobFilterComponent,
    JobFilterDetailComponent,
    JobFilterDialogComponent,
    JobFilterPopupComponent,
    JobFilterDeletePopupComponent,
    JobFilterDeleteDialogComponent,
    jobFilterRoute,
    jobFilterPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobFilterRoute,
    ...jobFilterPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobFilterComponent,
        JobFilterDetailComponent,
        JobFilterDialogComponent,
        JobFilterDeleteDialogComponent,
        JobFilterPopupComponent,
        JobFilterDeletePopupComponent,
    ],
    entryComponents: [
        JobFilterComponent,
        JobFilterDialogComponent,
        JobFilterPopupComponent,
        JobFilterDeleteDialogComponent,
        JobFilterDeletePopupComponent,
    ],
    providers: [
        JobFilterService,
        JobFilterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobFilterModule {}
