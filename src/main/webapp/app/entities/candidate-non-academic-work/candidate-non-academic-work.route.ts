import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import {
    CandidateNonAcademicWorkPopupComponent,
    CandidateNonAcademicWorkPopupNewComponent
} from './candidate-non-academic-work-dialog.component';

import { CandidateNonAcademicWorkComponent } from './candidate-non-academic-work.component';
import { CandidateNonAcademicWorkDetailComponent } from './candidate-non-academic-work-detail.component';

import { CandidateNonAcademicWorkDeletePopupComponent } from './candidate-non-academic-work-delete-dialog.component';

export const candidateNonAcademicWorkRoute: Routes = [
    {
        path: '',
        component: CandidateNonAcademicWorkComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-non-academic-work',
        component: CandidateNonAcademicWorkComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
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
        path: 'new-candidate-non-academic-work',
        component: CandidateNonAcademicWorkPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },

    {
        path: 'candidate-non-academic-work/:id/edit',
        component: CandidateNonAcademicWorkPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-non-academic-work/:id/delete',
        component: CandidateNonAcademicWorkDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-non-academic-work/edit',
        component: CandidateNonAcademicWorkPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-non-academic-work/delete',
        component: CandidateNonAcademicWorkDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateNonAcademicWork.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
