import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { CandidateCertificationPopupNewComponent } from '../../entities/candidate-certification/candidate-certification-dialog.component';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CandidateProfileComponent } from './candidate-profile-settings.component';
import { CandidateResolverService } from './candidate-profile-account-resolver.service';
import { CandidateLanguageProficiencyComponent } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.component';
import { CandidateEducationComponent } from '../../entities/candidate-education/candidate-education.component';
import { CandidateEmploymentComponent } from '../../entities/candidate-employment/candidate-employment.component';
import { CandidateCertificationComponent } from '../../entities/candidate-certification/candidate-certification.component';
import { CandidateEducationPopupNewComponent } from '../../entities/candidate-education/candidate-education-dialog.component';
import { CandidateEmploymentPopupNewComponent } from '../../entities/candidate-employment/candidate-employment-dialog.component';
import { CandidateLanguageProficiencyPopupNewComponent } from '../../entities/candidate-language-proficiency/candidate-language-proficiency-dialog.component';
import { CandidateNonAcademicWorkPopupNewComponent } from '../../entities/candidate-non-academic-work/candidate-non-academic-work-dialog.component';
import { CandidateNonAcademicWorkComponent } from '../../entities/candidate-non-academic-work/candidate-non-academic-work.component';
import { CandidateSkillsPopupNewComponent } from '../../entities/candidate-skills/candidate-skills-dialog.component';
import { CandidateSkillsComponent } from '../../entities/candidate-skills/candidate-skills.component';
import { CandidatePublicProfilePopupComponent } from './candidate-public-profile-popup.component';
import { AppliedJobsComponent } from './applied-job-by-candidate.component';
import { CandidatePrimarySettingsEditComponent } from './candidate-primary-settings-edit.component';
import { ShortListedJobsForCandidateComponent } from './shortlisted-for-job.component';
import { ProfilePicMgmtPopupComponent } from './profile-pic-mgmt-popup.component';
import { CandidateProfilePrimaryViewComponent } from './candidate-primary-settings-view.component';
import { CandidateProfileContactViewComponent } from './candidate-profile-contact-setting-view.component';
import { CandidateCareerInterestResolverService } from './candidate-profile-career-interest-resolver.service';
import { CandidateGenderResolverService } from './candidate-profile-gender-resolver.service';
import { CandidateContactSettingsEditComponent } from './candidate-profile-contact-settings-edit.component';

@Injectable()
export class JobResolvePagingParams implements Resolve<any> {
    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const candidateProfileRoutes: Routes = [
    {
        path: '',
        component: CandidateProfileComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        children: [
            {
                path: 'settings',
                children: [
                    {
                        path: '',
                        component: CandidateProfilePrimaryViewComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },
                        canActivate: [UserRouteAccessService],
                        outlet: 'primarySetting'
                    },
                    {
                        path: 'editUserPrimarySetting',
                        component: CandidatePrimarySettingsEditComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },
                        canActivate: [UserRouteAccessService],
                        resolve: {
                            jobCategory: CandidateCareerInterestResolverService,
                            gender: CandidateGenderResolverService
                        },
                        outlet: 'primarySetting'
                    },
                    {
                        path: '',
                        component: CandidateProfileContactViewComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },
                        canActivate: [UserRouteAccessService],
                        outlet: 'contactSetting'
                    },
                    {
                        path: 'editUserContactSetting',
                        component: CandidateContactSettingsEditComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },
                        canActivate: [UserRouteAccessService],
                        outlet: 'contactSetting'
                    }
                ]
            }
        ]
    },
    {
        path: 'appliedJobs',
        component: AppliedJobsComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        resolve: {
            pagingParams: JobResolvePagingParams
        }
    },
    {
        path: 'add-employment/:fromProfile',
        component: CandidateEmploymentPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'add-education/:fromProfile',
        component: CandidateEducationPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'add-skill/:fromProfile',
        component: CandidateSkillsPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'add-language/:fromProfile',
        component: CandidateLanguageProficiencyPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'add-cert/:fromProfile',
        component: CandidateCertificationPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'add-beyond-acad/:fromProfile',
        component: CandidateNonAcademicWorkPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shortListedForJob',
        component: ShortListedJobsForCandidateComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        resolve: {
            pagingParams: JobResolvePagingParams
        }
    }
];

export const candidateProfilePopupRoute: Routes = [
    {
        path: 'candidate-public-profile',
        component: CandidatePublicProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'update-picture',
        component: ProfilePicMgmtPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
