import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule } from '../../shared';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { GradzcircleCandidateProfileSharedModule } from '../../profiles/candidate/candidate-profile-shared.module';
import { QualificationService } from '../../entities/qualification/qualification.service';
import { CourseService } from '../../entities/course/course.service';
import { CollegeService } from '../../entities/college/college.service';
import { UniversityService } from '../../entities/university/university.service';
import { GradzcircleCandidateProjectModule } from '../candidate-project/candidate-project.module';

import {
    CandidateEducationService,
    CandidateEducationPopupService,
    // CandidateEducationComponent,
    CandidateEducationDetailComponent,
    CandidateEducationDialogComponent,
    CandidateEducationPopupComponent,
    CandidateEducationDeletePopupComponent,
    CandidateEducationDeleteDialogComponent,
    candidateEducationRoute,
    candidateEducationPopupRoute,
    //   CandidateEducationPopupNewComponent,
    CandidateEducationPopupServiceNew
} from './';

const ENTITY_STATES = [...candidateEducationRoute, ...candidateEducationPopupRoute];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleCandidateProfileSharedModule,
        GradzcircleCandidateProjectModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        // CandidateEducationComponent,
        CandidateEducationDetailComponent,
        //  CandidateEducationDialogComponent,
        CandidateEducationDeleteDialogComponent,
        CandidateEducationPopupComponent,
        CandidateEducationDeletePopupComponent
        //  CandidateEducationPopupNewComponent
    ],
    entryComponents: [
        //   CandidateEducationComponent,
        //  CandidateEducationDialogComponent,
        CandidateEducationPopupComponent,
        CandidateEducationDeleteDialogComponent,
        CandidateEducationDeletePopupComponent
        //   CandidateEducationPopupNewComponent
    ],
    providers: [
        CandidateEducationService,
        CandidateEducationPopupService,
        CandidateEducationPopupServiceNew,
        DataStorageService,
        QualificationService,
        CourseService,
        CollegeService,
        UniversityService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateEducationModule {}
