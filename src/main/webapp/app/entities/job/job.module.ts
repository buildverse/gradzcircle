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
  ShortListedCandidateListComponent,
  JobComponentAnonymous,
  JobListEmitterService,
  JobListPopupService,
  

} from './';
import {JobListForLinkedCandidateComponent, JobListForLinkedCandidatePopUpComponent} from './job-list-for-linked-candidate.component';
import {JobListForLinkedCandidate} from './job-list-linked-candidate.model';


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
    ShortListedCandidateListComponent,
    JobComponentAnonymous,
    JobListForLinkedCandidateComponent,
    JobListForLinkedCandidatePopUpComponent
    

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
    ShortListedCandidateListComponent,
    JobComponentAnonymous,
    JobListForLinkedCandidateComponent,
    JobListForLinkedCandidatePopUpComponent

  ],
  providers: [
    JobService,
    JobPopupService,
    // MatchTrackerService,
    JobPopupServiceNew,
    JobResolvePagingParams,
    JobListEmitterService,
    JobListPopupService

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
