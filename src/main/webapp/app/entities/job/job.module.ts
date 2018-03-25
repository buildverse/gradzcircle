import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule } from '../../shared';


import {
    JobService,
    JobPopupService,
    JobPopupServiceNew,
    JobComponent,
    JobDetailComponent,
    JobDialogComponent,
    JobPopupComponent,
    JobPopupComponentNew,
    JobDeletePopupComponent,
    JobDeleteDialogComponent,
    jobRoute,
    jobPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobRoute,
    ...jobPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
       
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobComponent,
        JobPopupComponentNew,
        JobDetailComponent,
        JobDialogComponent,
        JobDeleteDialogComponent,
        JobPopupComponent,
        JobDeletePopupComponent,
    ],
    entryComponents: [
        JobComponent,
        JobPopupComponentNew,
        JobDialogComponent,
        JobPopupComponent,
        JobDeleteDialogComponent,
        JobDeletePopupComponent,
    ],
    providers: [
        JobService,
        JobPopupService,
        JobPopupServiceNew
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
