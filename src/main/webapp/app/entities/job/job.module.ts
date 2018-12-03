import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {GradzcircleSharedModule} from '../../shared';
//import { GradzcircleTrackerModule, MatchTrackerComponent } from '../match-tracker/';
//import { MatchTrackerService } from '../match-tracker/match-tracker.service';
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
  JobViewPopupComponent,
  CandidateListDialogComponent,
  CandidateListPopupComponent,
  JobResolvePagingParams,
  MatchedCandidateListComponent,
  AppliedCandidateListComponent,
  ShortListedCandidateListComponent

} from './';


const ENTITY_STATES = [
  ...jobRoute,
  ...jobPopupRoute,
];

@NgModule({
  imports: [
    GradzcircleSharedModule,
    RouterModule.forChild(ENTITY_STATES)
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
    JobViewPopupComponent,
    CandidateListDialogComponent,
    CandidateListPopupComponent,
    MatchedCandidateListComponent,
    AppliedCandidateListComponent,
    ShortListedCandidateListComponent
    //MatchTrackerComponent
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
    JobViewPopupComponent,
    CandidateListDialogComponent,
    CandidateListPopupComponent,
    MatchedCandidateListComponent,
    AppliedCandidateListComponent,
    ShortListedCandidateListComponent
    //  MatchTrackerComponent
  ],
  providers: [
    JobService,
    JobPopupService,
    // MatchTrackerService,
    JobPopupServiceNew,
    JobResolvePagingParams


  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
