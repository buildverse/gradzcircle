import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidatePopupComponent } from './candidate-dialog.component';
import { CandidateDeletePopupComponent } from './candidate-delete-dialog.component';

export const candidateRoute: Routes = [
    {
        path: 'candidate',
        component: CandidateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'candidate/:id',
        component: CandidateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidatePopupRoute: Routes = [
    {
        path: 'candidate-new',
        component: CandidatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate/:id/edit',
        component: CandidatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate/:id/delete',
        component: CandidateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
