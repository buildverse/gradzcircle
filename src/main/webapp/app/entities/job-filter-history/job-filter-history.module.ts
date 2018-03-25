import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    JobFilterHistoryService,
    JobFilterHistoryPopupService,
    JobFilterHistoryComponent,
    JobFilterHistoryDetailComponent,
    JobFilterHistoryDialogComponent,
    JobFilterHistoryPopupComponent,
    JobFilterHistoryDeletePopupComponent,
    JobFilterHistoryDeleteDialogComponent,
    jobFilterHistoryRoute,
    jobFilterHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobFilterHistoryRoute,
    ...jobFilterHistoryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobFilterHistoryComponent,
        JobFilterHistoryDetailComponent,
        JobFilterHistoryDialogComponent,
        JobFilterHistoryDeleteDialogComponent,
        JobFilterHistoryPopupComponent,
        JobFilterHistoryDeletePopupComponent,
    ],
    entryComponents: [
        JobFilterHistoryComponent,
        JobFilterHistoryDialogComponent,
        JobFilterHistoryPopupComponent,
        JobFilterHistoryDeleteDialogComponent,
        JobFilterHistoryDeletePopupComponent,
    ],
    providers: [
        JobFilterHistoryService,
        JobFilterHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobFilterHistoryModule {}
