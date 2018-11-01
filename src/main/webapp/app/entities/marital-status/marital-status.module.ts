import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    MaritalStatusService,
    MaritalStatusPopupService,
    MaritalStatusComponent,
    MaritalStatusDetailComponent,
    MaritalStatusDialogComponent,
    MaritalStatusPopupComponent,
    MaritalStatusDeletePopupComponent,
    MaritalStatusDeleteDialogComponent,
    maritalStatusRoute,
    maritalStatusPopupRoute,
} from './';

const ENTITY_STATES = [
    ...maritalStatusRoute,
    ...maritalStatusPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MaritalStatusComponent,
        MaritalStatusDetailComponent,
        MaritalStatusDialogComponent,
        MaritalStatusDeleteDialogComponent,
        MaritalStatusPopupComponent,
        MaritalStatusDeletePopupComponent,
    ],
    entryComponents: [
        MaritalStatusComponent,
        MaritalStatusDialogComponent,
        MaritalStatusPopupComponent,
        MaritalStatusDeleteDialogComponent,
        MaritalStatusDeletePopupComponent,
    ],
    providers: [
        MaritalStatusService,
        MaritalStatusPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleMaritalStatusModule {}
