import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FroalaEditorModule } from 'angular-froala-wysiwyg';
import { GradzcircleSharedModule } from '../../shared';
import {HttpModule} from '@angular/http';
import { JsonpModule } from '@angular/http';

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
    CollegeResolverService,
    CourseResolverService,
    QualificationResolverService,
    CandidateEducationPopupComponentNew,
    CandidateEducationPopupServiceNew,
    EducationCollegeService

} from './';

const ENTITY_STATES = [
    ...candidateEducationRoute,
    ...candidateEducationPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        HttpModule,
        JsonpModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        FroalaEditorModule.forRoot()

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
        CollegeResolverService,
        CourseResolverService,
        QualificationResolverService,
        EducationCollegeService,

    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateEducationModule { }
