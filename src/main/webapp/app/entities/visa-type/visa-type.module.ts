import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    VisaTypeService,
    VisaTypePopupService,
    VisaTypeComponent,
    VisaTypeDetailComponent,
    VisaTypeDialogComponent,
    VisaTypePopupComponent,
    VisaTypeDeletePopupComponent,
    VisaTypeDeleteDialogComponent,
    visaTypeRoute,
    visaTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...visaTypeRoute,
    ...visaTypePopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VisaTypeComponent,
        VisaTypeDetailComponent,
        VisaTypeDialogComponent,
        VisaTypeDeleteDialogComponent,
        VisaTypePopupComponent,
        VisaTypeDeletePopupComponent,
    ],
    entryComponents: [
        VisaTypeComponent,
        VisaTypeDialogComponent,
        VisaTypePopupComponent,
        VisaTypeDeleteDialogComponent,
        VisaTypeDeletePopupComponent,
    ],
    providers: [
        VisaTypeService,
        VisaTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleVisaTypeModule {}
