import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CandidateProjectComponent } from './candidate-project.component';
import { CandidateProjectDetailComponent } from './candidate-project-detail.component';
import {
    CandidateProjectPopupComponent,
    CandidateEducationProjectPopupComponent,
    CandidateEmploymentProjectPopupComponent
} from './candidate-project-dialog.component';
import { CandidateProjectDeletePopupComponent } from './candidate-project-delete-dialog.component';

export const candidateProjectRoute: Routes = [
    {
        path: 'candidate-project',
        component: CandidateProjectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-project/:id',
        component: CandidateProjectDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateProjectPopupRoute: Routes = [
    {
        path: 'candidate-project-new',
        component: CandidateProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-education-project-new',
        component: CandidateEducationProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment-project-new',
        component: CandidateEmploymentProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },

    {
        path: 'candidate-project/edit',
        component: CandidateProjectPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-project/delete',
        component: CandidateProjectDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateProject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
