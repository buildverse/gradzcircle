import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    StatesService,
    StatesPopupService,
    StatesComponent,
    StatesDetailComponent,
    StatesDialogComponent,
    StatesPopupComponent,
    StatesDeletePopupComponent,
    StatesDeleteDialogComponent,
    statesRoute,
    statesPopupRoute,
    StatesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...statesRoute,
    ...statesPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        StatesComponent,
        StatesDetailComponent,
        StatesDialogComponent,
        StatesDeleteDialogComponent,
        StatesPopupComponent,
        StatesDeletePopupComponent,
    ],
    entryComponents: [
        StatesComponent,
        StatesDialogComponent,
        StatesPopupComponent,
        StatesDeleteDialogComponent,
        StatesDeletePopupComponent,
    ],
    providers: [
        StatesService,
        StatesPopupService,
        StatesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleStatesModule {}
