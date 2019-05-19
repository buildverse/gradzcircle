import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GradzcircleSharedModule, DataStorageService } from '../../shared';
import {
    CandidateLanguageProficiencyService,
    CandidateLanguageProficiencyPopupService,
    CandidateLanguageProficiencyPopupServiceNew,
    CandidateLanguageProficiencyComponent,
    CandidateLanguageProficiencyDetailComponent,
    CandidateLanguageProficiencyDialogComponent,
    CandidateLanguageProficiencyPopupComponent,
    CandidateLanguageProficiencyDeletePopupComponent,
    CandidateLanguageProficiencyDeleteDialogComponent,
    candidateLanguageProficiencyRoute,
    candidateLanguageProficiencyPopupRoute,
    CandidateLanguageProficiencyPopupComponentNew,
    CandidateLanguageResolverService
} from './';

const ENTITY_STATES = [
    ...candidateLanguageProficiencyRoute,
    ...candidateLanguageProficiencyPopupRoute,
];

@NgModule({
    imports: [
        GradzcircleSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CandidateLanguageProficiencyComponent,
        CandidateLanguageProficiencyDetailComponent,
        CandidateLanguageProficiencyDialogComponent,
        CandidateLanguageProficiencyDeleteDialogComponent,
        CandidateLanguageProficiencyPopupComponent,
        CandidateLanguageProficiencyPopupComponentNew,
        CandidateLanguageProficiencyDeletePopupComponent,
    ],
    entryComponents: [
        CandidateLanguageProficiencyComponent,
        CandidateLanguageProficiencyDialogComponent,
        CandidateLanguageProficiencyPopupComponent,
        CandidateLanguageProficiencyPopupComponentNew,
        CandidateLanguageProficiencyDeleteDialogComponent,
        CandidateLanguageProficiencyDeletePopupComponent,
    ],
    providers: [
        CandidateLanguageProficiencyService,
        CandidateLanguageProficiencyPopupService,
        CandidateLanguageProficiencyPopupServiceNew,
        CandidateLanguageResolverService,
        DataStorageService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GradzcircleCandidateLanguageProficiencyModule {}
