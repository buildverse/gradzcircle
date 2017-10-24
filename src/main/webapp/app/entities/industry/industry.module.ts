import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    IndustryService,
    IndustryPopupService,
    IndustryComponent,
    IndustryDetailComponent,
    IndustryDialogComponent,
    IndustryPopupComponent,
    IndustryDeletePopupComponent,
    IndustryDeleteDialogComponent,
    industryRoute,
    industryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...industryRoute,
    ...industryPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        IndustryComponent,
        IndustryDetailComponent,
        IndustryDialogComponent,
        IndustryDeleteDialogComponent,
        IndustryPopupComponent,
        IndustryDeletePopupComponent,
    ],
    entryComponents: [
        IndustryComponent,
        IndustryDialogComponent,
        IndustryPopupComponent,
        IndustryDeleteDialogComponent,
        IndustryDeletePopupComponent,
    ],
    providers: [
        IndustryService,
        IndustryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleIndustryModule {}
