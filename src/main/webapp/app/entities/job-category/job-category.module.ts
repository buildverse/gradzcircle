import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    JobCategoryService,
    JobCategoryPopupService,
    JobCategoryComponent,
    JobCategoryDetailComponent,
    JobCategoryDialogComponent,
    JobCategoryPopupComponent,
    JobCategoryDeletePopupComponent,
    JobCategoryDeleteDialogComponent,
    jobCategoryRoute,
    jobCategoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobCategoryRoute,
    ...jobCategoryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        JobCategoryComponent,
        JobCategoryDetailComponent,
        JobCategoryDialogComponent,
        JobCategoryDeleteDialogComponent,
        JobCategoryPopupComponent,
        JobCategoryDeletePopupComponent,
    ],
    entryComponents: [
        JobCategoryComponent,
        JobCategoryDialogComponent,
        JobCategoryPopupComponent,
        JobCategoryDeleteDialogComponent,
        JobCategoryDeletePopupComponent,
    ],
    providers: [
        JobCategoryService,
        JobCategoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobCategoryModule {}
