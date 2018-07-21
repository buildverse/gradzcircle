import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {GradzcircleSharedModule} from '../../shared';
import { FroalaEditorModule } from 'angular-froala-wysiwyg';

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
  JobEditMessageDialogComponent,
  JobEditMessagePopupComponent,
  JobRemoveDialogComponent,
  JobRemovePopupComponent,
  JobViewComponent,
  JobViewPopupComponent
} from './';


const ENTITY_STATES = [
  ...jobRoute,
  ...jobPopupRoute,
];

@NgModule({
  imports: [
    GradzcircleSharedModule,
    RouterModule.forRoot(ENTITY_STATES, {useHash: true}),
     FroalaEditorModule.forRoot()
  ],
  declarations: [
    JobComponent,
    JobPopupComponentNew,
    JobDetailComponent,
    JobDialogComponent,
    JobDeleteDialogComponent,
    JobEditMessageDialogComponent,
    JobEditMessagePopupComponent,
    JobPopupComponent,
    JobDeletePopupComponent,
    JobRemoveDialogComponent,
    JobRemovePopupComponent,
    JobViewComponent,
  JobViewPopupComponent
  ],
  entryComponents: [
    JobComponent,
    JobPopupComponentNew,
    JobDialogComponent,
    JobPopupComponent,
    JobDeleteDialogComponent,
    JobDeletePopupComponent,
    JobEditMessagePopupComponent,
    JobEditMessageDialogComponent,
    JobRemoveDialogComponent,
    JobRemovePopupComponent,
    JobViewComponent,
  JobViewPopupComponent
  ],
  providers: [
    JobService,
    JobPopupService,
    JobPopupServiceNew
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
