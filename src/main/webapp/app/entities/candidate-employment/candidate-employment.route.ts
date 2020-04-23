import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';

import { CandidateEmploymentComponent } from './candidate-employment.component';
import { CandidateEmploymentDetailComponent } from './candidate-employment-detail.component';
import { CandidateEmploymentPopupComponent, CandidateEmploymentPopupNewComponent } from './candidate-employment-dialog.component';
import { CandidateEmploymentDeletePopupComponent } from './candidate-employment-delete-dialog.component';

export const candidateEmploymentRoute: Routes = [
    {
        path: '',
        component: CandidateEmploymentComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-employment',
        component: CandidateEmploymentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-employment/:id',
        component: CandidateEmploymentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateEmploymentPopupRoute: Routes = [
    {
        path: 'candidate-employment-new',
        component: CandidateEmploymentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'new-candidate-employment',
        component: CandidateEmploymentPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment/:id/edit',
        component: CandidateEmploymentPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment/edit',
        component: CandidateEmploymentPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment/:id/delete',
        component: CandidateEmploymentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment/delete',
        component: CandidateEmploymentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
