import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule, DataService } from '../../shared';


import {
    CandidateEducationService,
    CandidateEducationPopupService,
    CandidateEducationComponent,
    CandidateEducationDetailComponent,
    CandidateEducationDialogComponent,
    CandidateEducationPopupComponent,
    CandidateEducationDeletePopupComponent,
    CandidateEducationDeleteDialogComponent,
    candidateEducationRoute,
    candidateEducationPopupRoute,
   // CollegeResolverService,
   // CourseResolverService,
   // QualificationResolverService,
    CandidateEducationPopupComponentNew,
    CandidateEducationPopupServiceNew,

} from './';

const ENTITY_STATES = [
    ...candidateEducationRoute,
    ...candidateEducationPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)

    ],
    declarations: [
        CandidateEducationComponent,
        CandidateEducationDetailComponent,
        CandidateEducationDialogComponent,
        CandidateEducationDeleteDialogComponent,
        CandidateEducationPopupComponent,
        CandidateEducationDeletePopupComponent,
        CandidateEducationPopupComponentNew
    ],
    entryComponents: [
        CandidateEducationComponent,
        CandidateEducationDialogComponent,
        CandidateEducationPopupComponent,
        CandidateEducationDeleteDialogComponent,
        CandidateEducationDeletePopupComponent,
        CandidateEducationPopupComponentNew
    ],
    providers: [
        CandidateEducationService,
        CandidateEducationPopupService,
        CandidateEducationPopupServiceNew,
        DataService
       // CollegeResolverService,
      //  CourseResolverService,
      //  QualificationResolverService

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateEducationModule { }
