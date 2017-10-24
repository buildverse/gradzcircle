import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CandidateProfileComponent } from './candidate-profile.component';
import { CandidateProfileAboutMeEditComponent } from './candidate-about-me-edit.component';
import { CandidateResolverService } from './candidate-profile-account-resolver.service';
import { CandidateCareerInterestResolverService } from './candidate-profile-career-interest-resolver.service';
import { CandidateProfileAboutMeDetailsComponent } from './candidate-about-me-details.component'
import { CandidateMaritalStatusResolverService } from './candidate-profile-marital-status-resolver.service';
import { CountryResolverService } from './candidate-profile-country-resolver.service';
import { CandidateGenderResolverService } from './candidate-profile-gender-resolver.service';
import { CandidateVisaResolverService } from './candidate-profile-visa-resolver.service';
import { CandidateLanguageResolverService } from './candidate-profile-language-resolver.service';
import { CandidateAboutMeEditGuard } from './candidate-profile-about-me-edit-guard';
import { CandidateNationalityResolverService } from './candidate-profile-nationality-resolver.service';
import { CandidateDetailResolverService } from './candidate-detail-resolver.service';
import { CandidateLanguageProficiencyDetailsComponent } from './candidate-about-me-language-details.component';
import { CandidateLanguageProficiencyResolverService } from './candidate-language-proficiency-resolver.service';
import { CandidateProfileLanguaugeEditComponent } from './candidate-about-me-language-edit-component';
import { CandidateLanguageProficiencyComponent } from '../../entities/candidate-language-proficiency/candidate-language-proficiency.component';
import { CandidateEducationComponent } from '../../entities/candidate-education/candidate-education.component';
import { CandidateEmploymentComponent } from '../../entities/candidate-employment/candidate-employment.component';
import { CandidateCertificationComponent } from '../../entities/candidate-certification/candidate-certification.component';
import { CandidateNonAcademicWorkComponent } from '../../entities/candidate-non-academic-work/candidate-non-academic-work.component';
import { CandidatePublicProfilePopupComponent } from './candidate-public-profile-popup.component';

export const candidateProfileRoutes: Routes = [
    {
        path: 'candidate-profile',
        component: CandidateProfileComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        resolve: {
            candidate: CandidateResolverService
        
        },
        children: [
            {
                path: '',
                component: CandidateProfileAboutMeDetailsComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },

                canActivate: [UserRouteAccessService],
                resolve: {
                    candidate: CandidateDetailResolverService
                    //candidateLanguageProficiency: CandidateLanguageProficiencyResolverService
                },
                children: [
                    {
                        path: '',
                        component: CandidateLanguageProficiencyComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },

                        canActivate: [UserRouteAccessService]
                    }
                ]

            },
            {
                path: 'edit/:id',
                component: CandidateProfileAboutMeEditComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },

                canActivate: [UserRouteAccessService],
                canDeactivate: [CandidateAboutMeEditGuard],
                resolve: {

                    candidate: CandidateDetailResolverService,
                    jobCategory: CandidateCareerInterestResolverService,
                    maritalStatus: CandidateMaritalStatusResolverService,
                    country: CountryResolverService,
                    gender: CandidateGenderResolverService,
                    visaType: CandidateVisaResolverService,
                    nationality: CandidateNationalityResolverService
                },
                children: [
                    {
                        path: '',
                        component: CandidateLanguageProficiencyComponent,
                        data: {
                            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                            pageTitle: 'gradzcircleApp.candidate.home.title'
                        },

                        canActivate: [UserRouteAccessService]

                    }
                ]
            },

            {
                path: 'education',
                component: CandidateEducationComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },
                canActivate: [UserRouteAccessService],
            },
             {
                path: 'employment',
                component: CandidateEmploymentComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },
                canActivate: [UserRouteAccessService],
            },
             {
                path: 'certifications',
                component: CandidateCertificationComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },
                canActivate: [UserRouteAccessService],
            },
            {
                path: 'beyondAcademics',
                component: CandidateNonAcademicWorkComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },
                canActivate: [UserRouteAccessService],
            },
            // {
            //     path : 'candidate-public-profile',
            //     component : CandidatePublicProfileComponent,
            //     data: {
            //         authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            //         pageTitle: 'gradzcircleApp.candidate.home.title'
            //     },
            //     canActivate: [UserRouteAccessService],
            // }
        ]
    },
];

export const candidateProfilePopupRoute: Routes = [
    {
        path : 'candidate-public-profile/:id',
        component : CandidatePublicProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
]