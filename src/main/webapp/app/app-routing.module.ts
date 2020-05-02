import { NgModule } from '@angular/core';
import { RouterModule, LoadChildren, PreloadAllModules } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#GradzcircleAdminModule'
                },
                {
                    path: 'candidate-profile',
                    loadChildren: './profiles/candidate/candidate-profile.module#CandidateProfileModule'
                },
                {
                    path: 'candidatePreview',
                    loadChildren: './entities/candidate/candidate.module#GradzcircleCandidateModule'
                },
                {
                    path: 'viewjobs',
                    loadChildren: './entities/job/job.module#GradzcircleJobModule'
                },
                {
                    path: 'corp',
                    loadChildren: './entities/job/job.module#GradzcircleJobModule'
                },
                {
                    path: 'corporate',
                    loadChildren: './entities/corporate/corporate.module#GradzcircleCorporateModule'
                },
                {
                    path: 'education',
                    loadChildren: './entities/candidate-education/candidate-education.module#GradzcircleCandidateEducationModule'
                },
                {
                    path: 'languages',
                    loadChildren:
                        './entities/candidate-language-proficiency/candidate-language-proficiency.module#GradzcircleCandidateLanguageProficiencyModule'
                },
                {
                    path: 'certification',
                    loadChildren:
                        './entities/candidate-certification/candidate-certification.module#GradzcircleCandidateCertificationModule'
                },
                {
                    path: 'employment',
                    loadChildren: './entities/candidate-employment/candidate-employment.module#GradzcircleCandidateEmploymentModule'
                },
                {
                    path: 'beyondAcademics',
                    loadChildren:
                        './entities/candidate-non-academic-work/candidate-non-academic-work.module#GradzcircleCandidateNonAcademicWorkModule'
                },
                {
                    path: 'languages',
                    loadChildren:
                        './entities/candidate-language-proficiency/candidate-language-proficiency.module#GradzcircleCandidateLanguageProficiencyModule'
                },
                {
                    path: 'skill',
                    loadChildren: './entities/candidate-skills/candidate-skills.module#GradzcircleCandidateSkillsModule'
                }
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class GradzcircleAppRoutingModule {}
