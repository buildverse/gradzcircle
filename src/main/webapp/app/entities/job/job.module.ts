import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';
import {GradzcircleSharedModule} from '../../shared';

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
  AddMoreCandidatesToJobPopUpComponent,
  AddMoreCandidatesToPopupService,
  AddMoreCandidatesToJobComponent,
  MinDirective,
  JobViewForCandidateComponent,
  JobViewPopupForCandidateComponent
} from './';
import {JobListForLinkedCandidateComponent, JobListForLinkedCandidatePopUpComponent} from './job-list-for-linked-candidate.component';



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
    JobListForLinkedCandidatePopUpComponent,
    AddMoreCandidatesToJobPopUpComponent,
    AddMoreCandidatesToJobComponent,
    MinDirective,
    JobViewForCandidateComponent,
    JobViewPopupForCandidateComponent
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
    JobListForLinkedCandidatePopUpComponent,
    AddMoreCandidatesToJobPopUpComponent,
    AddMoreCandidatesToJobComponent,
    JobViewForCandidateComponent,
    JobViewPopupForCandidateComponent
  ],
  providers: [
    JobService,
    JobPopupService,
    // MatchTrackerService,
    JobPopupServiceNew,
    JobResolvePagingParams,
    JobListEmitterService,
    JobListPopupService,
    AddMoreCandidatesToPopupService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
