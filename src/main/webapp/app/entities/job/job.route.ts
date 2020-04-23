import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';
import { JobComponent } from './job.component';
import { JobDetailComponent } from './job-detail.component';
import { JobPopupComponent, JobPopupNewComponent } from './job-dialog.component';
import { JobViewPopupComponent } from './job-view.component';
import { JobRemovePopupComponent } from './job-remove-dialog.component';
import { JobDeletePopupComponent } from './job-delete-dialog.component';
import { JobEditMessagePopupComponent } from './job-edit-message-dialog.component';
import { CandidateListPopupComponent } from '../candidate-list/candidate-list-dialog.component';
import { AddMoreCandidatesToJobPopUpComponent } from './add-more-candidate-to-job-popup.component';
import { MatchedCandidateListComponent } from './matched-candidate-list.component';
import { AppliedCandidateListComponent } from './applied-candidate-list.component';
import { ShortListedCandidateListComponent } from './short-listed-candidate-list.component';
import { JobAnonymousComponent } from './job-for-anonymous.component';
import { JobListForLinkedCandidatePopUpComponent } from './job-list-for-linked-candidate.component';
import { JobViewPopupForCandidateComponent } from './job-view-candidate.component';

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

export const jobRoute: Routes = [
    {
        path: '',
        component: JobAnonymousComponent,
        runGuardsAndResolvers: 'always',
        resolve: {
            pagingParams: JobResolvePagingParams
        },
        data: {
            pageTitle: 'gradzcircleApp.job.home.title'
        }
    },
    {
        path: 'job',
        component: JobComponent,
        resolve: {
            pagingParams: JobResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'job/:id',
        component: JobDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'matchedCandidateList',
        component: MatchedCandidateListComponent,
        resolve: {
            pagingParams: JobResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'appliedCandidateList',
        component: AppliedCandidateListComponent,
        resolve: {
            pagingParams: JobResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'shortListedCandidateList',
        component: ShortListedCandidateListComponent,
        resolve: {
            pagingParams: JobResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    // matchTrackerRoute
];

export const jobPopupRoute: Routes = [
    {
        path: 'job-new',
        component: JobPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'new-job',
        component: JobPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/edit',
        component: JobPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/view',
        component: JobViewPopupComponent,
        data: {
            // authorities: ['ROLE_USER','ROLE_CORPORATE','ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/viewJobForGuest',
        component: JobViewPopupComponent,
        data: {
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'job/viewJobForCandidate',
        component: JobViewPopupForCandidateComponent,
        data: {
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'job/:id/delete',
        component: JobDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/remove',
        component: JobRemovePopupComponent,
        data: {
            authorities: ['ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'jobEditMessage',
        component: JobEditMessagePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidateList',
        component: CandidateListPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-list-for-linked-candidate',
        component: JobListForLinkedCandidatePopUpComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'addMoreCandidatesToJob',
        component: AddMoreCandidatesToJobPopUpComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
