import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CandidateEmploymentComponent } from './candidate-employment.component';
import { CandidateEmploymentDetailComponent } from './candidate-employment-detail.component';
import { CandidateEmploymentPopupComponent,CandidateEmploymentPopupComponentNew } from './candidate-employment-dialog.component';
import { CandidateEmploymentDeletePopupComponent } from './candidate-employment-delete-dialog.component';

import { Principal } from '../../shared';

export const candidateEmploymentRoute: Routes = [
    {
        path: 'candidate-employment',
        component: CandidateEmploymentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
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
    {        path: 'candidate-employment-new/:id',
        component: CandidateEmploymentPopupComponentNew,
        data: {
            authorities: ['ROLE_USER','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },


    {
        path: 'candidate-employment/:id/edit',
        component: CandidateEmploymentPopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-employment/:id/delete',
        component: CandidateEmploymentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
