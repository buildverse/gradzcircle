import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    FilterCategoryService,
    FilterCategoryPopupService,
    FilterCategoryComponent,
    FilterCategoryDetailComponent,
    FilterCategoryDialogComponent,
    FilterCategoryPopupComponent,
    FilterCategoryDeletePopupComponent,
    FilterCategoryDeleteDialogComponent,
    filterCategoryRoute,
    filterCategoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...filterCategoryRoute,
    ...filterCategoryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FilterCategoryComponent,
        FilterCategoryDetailComponent,
        FilterCategoryDialogComponent,
        FilterCategoryDeleteDialogComponent,
        FilterCategoryPopupComponent,
        FilterCategoryDeletePopupComponent,
    ],
    entryComponents: [
        FilterCategoryComponent,
        FilterCategoryDialogComponent,
        FilterCategoryPopupComponent,
        FilterCategoryDeleteDialogComponent,
        FilterCategoryDeletePopupComponent,
    ],
    providers: [
        FilterCategoryService,
        FilterCategoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleFilterCategoryModule {}
