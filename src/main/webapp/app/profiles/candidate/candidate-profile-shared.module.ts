import {
    CandidateCertificationPopupNewComponent,
    CandidateCertificationDialogComponent
} from '../../entities/candidate-certification/candidate-certification-dialog.component';
import { CandidateCertificationPopupServiceNew } from '../../entities/candidate-certification/candidate-certification-popup-new.service';
import { CandidateCertificationComponent } from '../../entities/candidate-certification/candidate-certification.component';
import { CandidateCertificationService } from '../../entities/candidate-certification/candidate-certification.service';
import {
    CandidateEducationPopupNewComponent,
    CandidateEducationDialogComponent
} from '../../entities/candidate-education/candidate-education-dialog.component';
import { CandidateEducationPopupServiceNew } from '../../entities/candidate-education/candidate-education-popup-new.service';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CandidateEducationComponent } from '../../entities/candidate-education/candidate-education.component';
import { GradzcircleSharedModule } from '../../shared/shared.module';
import { CandidateEducationService } from '../../entities/candidate-education/candidate-education.service';
import {
    CandidateEmploymentPopupNewComponent,
    CandidateEmploymentDialogComponent
} from '../../entities/candidate-employment/candidate-employment-dialog.component';
import { CandidateEmploymentPopupServiceNew } from '../../entities/candidate-employment/candidate-employment-popup-new.service';
import { CandidateEmploymentComponent } from '../../entities/candidate-employment/candidate-employment.component';
import { CandidateEmploymentService } from '../../entities/candidate-employment/candidate-employment.service';
import {
    CandidateLanguageProficiencyPopupNewComponent,
    CandidateLanguageProficiencyDialogComponent
} from '../../entities/candidate-language-proficiency/candidate-language-proficiency-dialog.component';
import { CandidateLanguageProficiencyPopupServiceNew } from '../../entities/candidate-language-proficiency/candidate-language-proficiency-popup-new.service';
import { CandidateLanguageProficiencyComponent } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.component';
import { CandidateLanguageProficiencyService } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.service';
import {
    CandidateNonAcademicWorkPopupNewComponent,
    CandidateNonAcademicWorkDialogComponent
} from '../../entities/candidate-non-academic-work/candidate-non-academic-work-dialog.component';
import { CandidateNonAcademicWorkPopupServiceNew } from '../../entities/candidate-non-academic-work/candidate-non-academic-work-popup-new.service';
import { CandidateNonAcademicWorkComponent } from '../../entities/candidate-non-academic-work/candidate-non-academic-work.component';
import { CandidateNonAcademicWorkService } from '../../entities/candidate-non-academic-work/candidate-non-academic-work.service';
import {
    CandidateSkillsPopupNewComponent,
    CandidateSkillsDialogComponent
} from '../../entities/candidate-skills/candidate-skills-dialog.component';
import { CandidateSkillsPopupServiceNew } from '../../entities/candidate-skills/candidate-skills-new-popup.service';
import { CandidateSkillsComponent } from '../../entities/candidate-skills/candidate-skills.component';
import { CandidateSkillsService } from '../../entities/candidate-skills/candidate-skills.service';
import { CollegeService } from '../../entities/college/college.service';
import { CourseService } from '../../entities/course/course.service';
import { EmploymentTypeService } from '../../entities/employment-type/employment-type.service';
import { JobTypeService } from '../../entities/job-type/job-type.service';
import { JobService } from '../../entities/job/job.service';
import { LanguageService } from '../../entities/language/language.service';
import { QualificationService } from '../../entities/qualification/qualification.service';
import { SkillsService } from '../../entities/skills/skills.service';
import { UniversityService } from '../../entities/university/university.service';

@NgModule({
    imports: [GradzcircleSharedModule, RouterModule],
    declarations: [
        CandidateEducationComponent,
        CandidateEmploymentComponent,
        CandidateCertificationComponent,
        CandidateNonAcademicWorkComponent,
        CandidateSkillsComponent,
        CandidateLanguageProficiencyComponent,
        CandidateEmploymentPopupNewComponent,
        CandidateEmploymentDialogComponent,
        CandidateEducationPopupNewComponent,
        CandidateSkillsPopupNewComponent,
        CandidateLanguageProficiencyPopupNewComponent,
        CandidateCertificationPopupNewComponent,
        CandidateNonAcademicWorkPopupNewComponent,
        CandidateEducationDialogComponent,
        CandidateLanguageProficiencyDialogComponent,
        CandidateNonAcademicWorkDialogComponent,
        CandidateSkillsDialogComponent,
        CandidateCertificationDialogComponent
    ],

    exports: [
        CandidateEducationComponent,
        CandidateEmploymentComponent,
        CandidateCertificationComponent,
        CandidateNonAcademicWorkComponent,
        CandidateSkillsComponent,
        CandidateLanguageProficiencyComponent,
        CandidateEmploymentPopupNewComponent,
        CandidateEmploymentDialogComponent,
        CandidateEducationPopupNewComponent,
        CandidateSkillsPopupNewComponent,
        CandidateLanguageProficiencyPopupNewComponent,
        CandidateCertificationPopupNewComponent,
        CandidateNonAcademicWorkPopupNewComponent,
        CandidateEducationDialogComponent,
        CandidateLanguageProficiencyDialogComponent,
        CandidateNonAcademicWorkDialogComponent,
        CandidateSkillsDialogComponent,
        CandidateCertificationDialogComponent
    ],

    schemas: [CUSTOM_ELEMENTS_SCHEMA],

    entryComponents: [
        CandidateEmploymentDialogComponent,
        CandidateEducationDialogComponent,
        CandidateLanguageProficiencyDialogComponent,
        CandidateNonAcademicWorkDialogComponent,
        CandidateSkillsDialogComponent,
        CandidateCertificationDialogComponent
    ],

    providers: [
        CandidateEducationService,
        CandidateEmploymentPopupServiceNew,
        CandidateEmploymentService,
        EmploymentTypeService,
        JobTypeService,
        CandidateEducationPopupServiceNew,
        QualificationService,
        CourseService,
        CollegeService,
        UniversityService,
        SkillsService,
        CandidateSkillsPopupServiceNew,
        CandidateSkillsService,
        CandidateCertificationPopupServiceNew,
        CandidateCertificationService,
        CandidateNonAcademicWorkPopupServiceNew,
        CandidateNonAcademicWorkService,
        CandidateLanguageProficiencyPopupServiceNew,
        CandidateLanguageProficiencyService,
        LanguageService,
        JobService
    ]
})
export class GradzcircleCandidateProfileSharedModule {}
