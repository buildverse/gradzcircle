import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    QualificationService,
    QualificationPopupService,
    QualificationComponent,
    QualificationDetailComponent,
    QualificationDialogComponent,
    QualificationPopupComponent,
    QualificationDeletePopupComponent,
    QualificationDeleteDialogComponent,
    qualificationRoute,
    qualificationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...qualificationRoute,
    ...qualificationPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        QualificationComponent,
        QualificationDetailComponent,
        QualificationDialogComponent,
        QualificationDeleteDialogComponent,
        QualificationPopupComponent,
        QualificationDeletePopupComponent,
    ],
    entryComponents: [
        QualificationComponent,
        QualificationDialogComponent,
        QualificationPopupComponent,
        QualificationDeleteDialogComponent,
        QualificationDeletePopupComponent,
    ],
    providers: [
        QualificationService,
        QualificationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleQualificationModule {}
