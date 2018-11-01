import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    AuditService,
    AuditPopupService,
    AuditComponent,
    AuditDetailComponent,
    AuditDialogComponent,
    AuditPopupComponent,
    AuditDeletePopupComponent,
    AuditDeleteDialogComponent,
    auditRoute,
    auditPopupRoute,
} from './';

const ENTITY_STATES = [
    ...auditRoute,
    ...auditPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AuditComponent,
        AuditDetailComponent,
        AuditDialogComponent,
        AuditDeleteDialogComponent,
        AuditPopupComponent,
        AuditDeletePopupComponent,
    ],
    entryComponents: [
        AuditComponent,
        AuditDialogComponent,
        AuditPopupComponent,
        AuditDeleteDialogComponent,
        AuditDeletePopupComponent,
    ],
    providers: [
        AuditService,
        AuditPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleAuditModule {}
