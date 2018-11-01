import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule } from '../../shared';
import {
    ErrorMessagesService,
    ErrorMessagesPopupService,
    ErrorMessagesComponent,
    ErrorMessagesDetailComponent,
    ErrorMessagesDialogComponent,
    ErrorMessagesPopupComponent,
    ErrorMessagesDeletePopupComponent,
    ErrorMessagesDeleteDialogComponent,
    errorMessagesRoute,
    errorMessagesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...errorMessagesRoute,
    ...errorMessagesPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ErrorMessagesComponent,
        ErrorMessagesDetailComponent,
        ErrorMessagesDialogComponent,
        ErrorMessagesDeleteDialogComponent,
        ErrorMessagesPopupComponent,
        ErrorMessagesDeletePopupComponent,
    ],
    entryComponents: [
        ErrorMessagesComponent,
        ErrorMessagesDialogComponent,
        ErrorMessagesPopupComponent,
        ErrorMessagesDeleteDialogComponent,
        ErrorMessagesDeletePopupComponent,
    ],
    providers: [
        ErrorMessagesService,
        ErrorMessagesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleErrorMessagesModule {}
