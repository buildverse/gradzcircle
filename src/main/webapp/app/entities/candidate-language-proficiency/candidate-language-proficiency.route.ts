import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CandidateLanguageProficiencyComponent } from './candidate-language-proficiency.component';
import { CandidateLanguageProficiencyDetailComponent } from './candidate-language-proficiency-detail.component';
import { CandidateLanguageProficiencyPopupComponent } from './candidate-language-proficiency-dialog.component';
import { CandidateLanguageProficiencyPopupComponentNew } from './candidate-language-proficiency-dialog.component';
import { CandidateLanguageProficiencyDeletePopupComponent } from './candidate-language-proficiency-delete-dialog.component';
import { CandidateLanguageResolverService } from './candidate-language-proficiency-current.resolver.service';

import { Principal } from '../../shared';

export const candidateLanguageProficiencyRoute: Routes = [
    {
        path: 'candidate-language-proficiency',
        component: CandidateLanguageProficiencyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'candidate-language-proficiency/:id',
        component: CandidateLanguageProficiencyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateLanguageProficiencyPopupRoute: Routes = [
    {
        path: 'candidate-language-proficiency-new',
        component: CandidateLanguageProficiencyPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
    path: 'new-candidate-language-proficiency',
        component: CandidateLanguageProficiencyPopupComponentNew,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        resolve: {
            currentLanguageSelections : CandidateLanguageResolverService
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup',
    },
    {
        path: 'candidate-language-proficiency/:id/edit',
        component: CandidateLanguageProficiencyPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-language-proficiency/:id/delete',
        component: CandidateLanguageProficiencyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-language-proficiency/edit',
        component: CandidateLanguageProficiencyPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-language-proficiency/delete',
        component: CandidateLanguageProficiencyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateLanguageProficiency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
