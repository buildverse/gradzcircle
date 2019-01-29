import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    ProfileCategoryService,
    ProfileCategoryPopupService,
    ProfileCategoryComponent,
    ProfileCategoryDetailComponent,
    ProfileCategoryDialogComponent,
    ProfileCategoryPopupComponent,
    ProfileCategoryDeletePopupComponent,
    ProfileCategoryDeleteDialogComponent,
    profileCategoryRoute,
    profileCategoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...profileCategoryRoute,
    ...profileCategoryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProfileCategoryComponent,
        ProfileCategoryDetailComponent,
        ProfileCategoryDialogComponent,
        ProfileCategoryDeleteDialogComponent,
        ProfileCategoryPopupComponent,
        ProfileCategoryDeletePopupComponent,
    ],
    entryComponents: [
        ProfileCategoryComponent,
        ProfileCategoryDialogComponent,
        ProfileCategoryPopupComponent,
        ProfileCategoryDeleteDialogComponent,
        ProfileCategoryDeletePopupComponent,
    ],
    providers: [
        ProfileCategoryService,
        ProfileCategoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleProfileCategoryModule {}
