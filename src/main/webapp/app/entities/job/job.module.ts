import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule } from '../../shared';
import { CollegeService } from '../college/college.service';
import { CourseService } from '../course/course.service';
import { EmploymentTypeService } from '../employment-type/employment-type.service';
import { FilterService } from '../filter/filter.service';
import { GenderService } from '../gender/gender.service';
import { JobTypeService } from '../job-type/job-type.service';
import { LanguageService } from '../language/language.service';
import { QualificationService } from '../qualification/qualification.service';
import { SkillsService } from '../skills/skills.service';
import { UniversityService } from '../university/university.service';

import {
    JobService,
    JobPopupService,
    JobPopupServiceNew,
    JobComponent,
    JobDetailComponent,
    JobDialogComponent,
    JobPopupComponent,
    JobPopupNewComponent,
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
    JobAnonymousComponent,
    JobListEmitterService,
    JobListPopupService,
    AddMoreCandidatesToJobPopUpComponent,
    AddMoreCandidatesToPopupService,
    AddMoreCandidatesToJobComponent,
    MinDirective
} from './';
import { JobListForLinkedCandidateComponent, JobListForLinkedCandidatePopUpComponent } from './job-list-for-linked-candidate.component';

const ENTITY_STATES = [...jobRoute, ...jobPopupRoute];

@NgModule({
    imports: [GradzcircleSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        JobComponent,
        JobPopupNewComponent,
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
        JobAnonymousComponent,
        JobListForLinkedCandidateComponent,
        JobListForLinkedCandidatePopUpComponent,
        AddMoreCandidatesToJobPopUpComponent,
        AddMoreCandidatesToJobComponent,
        MinDirective
    ],
    entryComponents: [
        JobComponent,
        JobPopupNewComponent,
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
        JobAnonymousComponent,
        JobListForLinkedCandidateComponent,
        JobListForLinkedCandidatePopUpComponent,
        AddMoreCandidatesToJobPopUpComponent,
        AddMoreCandidatesToJobComponent
    ],
    providers: [
        JobService,
        JobPopupService,
        // MatchTrackerService,
        JobPopupServiceNew,
        JobResolvePagingParams,
        JobListEmitterService,
        JobListPopupService,
        AddMoreCandidatesToPopupService,
        JobTypeService,
        EmploymentTypeService,
        QualificationService,
        CourseService,
        CollegeService,
        UniversityService,
        SkillsService,
        LanguageService,
        FilterService,
        GenderService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleJobModule {}
