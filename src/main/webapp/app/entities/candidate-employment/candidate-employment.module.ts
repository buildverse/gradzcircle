import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GradzcircleSharedModule } from '../../shared';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { GradzcircleCandidateProjectModule } from '../candidate-project/candidate-project.module';
import { GradzcircleCandidateProfileSharedModule } from '../../profiles/candidate/candidate-profile-shared.module';
import { EmploymentTypeService } from '../employment-type/employment-type.service';
import { JobTypeService } from '../job-type/job-type.service';

import {
    CandidateEmploymentService,
    CandidateEmploymentPopupService,
    CandidateEmploymentDetailComponent,
    //    CandidateEmploymentDialogComponent,
    CandidateEmploymentPopupComponent,
    CandidateEmploymentDeletePopupComponent,
    CandidateEmploymentDeleteDialogComponent,
    candidateEmploymentRoute,
    candidateEmploymentPopupRoute,
    //   CandidateEmploymentPopupNewComponent,
    CandidateEmploymentPopupServiceNew
} from './';

const ENTITY_STATES = [...candidateEmploymentRoute, ...candidateEmploymentPopupRoute];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        GradzcircleCandidateProjectModule,
        GradzcircleCandidateProfileSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateEmploymentDetailComponent,
        //    CandidateEmploymentDialogComponent,
        CandidateEmploymentDeleteDialogComponent,
        CandidateEmploymentPopupComponent,
        CandidateEmploymentDeletePopupComponent
        //       CandidateEmploymentPopupNewComponent
    ],
    entryComponents: [
        //      CandidateEmploymentDialogComponent,
        CandidateEmploymentPopupComponent,
        CandidateEmploymentDeleteDialogComponent,
        CandidateEmploymentDeletePopupComponent
        //       CandidateEmploymentPopupNewComponent
    ],
    providers: [
        CandidateEmploymentService,
        CandidateEmploymentPopupService,
        CandidateEmploymentPopupServiceNew,
        DataStorageService,
        EmploymentTypeService,
        JobTypeService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateEmploymentModule {}
