import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    JobHistoryService,
    JobHistoryPopupService,
    JobHistoryComponent,
    JobHistoryDetailComponent,
    JobHistoryDialogComponent,
    JobHistoryPopupComponent,
    JobHistoryDeletePopupComponent,
    JobHistoryDeleteDialogComponent,
    jobHistoryRoute,
    jobHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobHistoryRoute,
    ...jobHistoryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobHistoryComponent,
        JobHistoryDetailComponent,
        JobHistoryDialogComponent,
        JobHistoryDeleteDialogComponent,
        JobHistoryPopupComponent,
        JobHistoryDeletePopupComponent,
    ],
    entryComponents: [
        JobHistoryComponent,
        JobHistoryDialogComponent,
        JobHistoryPopupComponent,
        JobHistoryDeleteDialogComponent,
        JobHistoryDeletePopupComponent,
    ],
    providers: [
        JobHistoryService,
        JobHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobHistoryModule {}
