import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CandidateNonAcademicWorkPopupComponent,CandidateNonAcademicWorkPopupComponentNew } from './candidate-non-academic-work-dialog.component';

import { CandidateNonAcademicWorkComponent } from './candidate-non-academic-work.component';
import { CandidateNonAcademicWorkDetailComponent } from './candidate-non-academic-work-detail.component';

import { CandidateNonAcademicWorkDeletePopupComponent } from './candidate-non-academic-work-delete-dialog.component';
import { Principal } from '../../shared';

export const candidateNonAcademicWorkRoute: Routes = [
    {
        path: 'candidate-non-academic-work',
        component: CandidateNonAcademicWorkComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'candidate-non-academic-work/:id',
        component: CandidateNonAcademicWorkDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateNonAcademicWorkPopupRoute: Routes = [
    {
        path: 'candidate-non-academic-work-new',
        component: CandidateNonAcademicWorkPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
    path: 'candidate-non-academic-work-new/:id',
    component: CandidateNonAcademicWorkPopupComponentNew,
    data: {
        authorities: ['ROLE_USER','ROLE_CANDIDATE'],
        pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
    },

    {
        path: 'candidate-non-academic-work/:id/edit',
        component: CandidateNonAcademicWorkPopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-non-academic-work/:id/delete',
        component: CandidateNonAcademicWorkDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
