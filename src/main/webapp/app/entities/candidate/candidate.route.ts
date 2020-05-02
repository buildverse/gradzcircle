import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { CandidatePublicProfilePopupComponent } from '../../shared/candidate-public-profile/candidate-public-profile-popup.component';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidatePopupComponent } from './candidate-dialog.component';
import { CandidateDeletePopupComponent } from './candidate-delete-dialog.component';
import { CandidatePreviewComponent } from './candidate-preview.component';

@Injectable()
export class CandidatePagingParams implements Resolve<any> {
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

export const candidateRoute: Routes = [
    {
        path: 'candidate',
        component: CandidateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        resolve: {
            pagingParams: CandidatePagingParams
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate/:id',
        component: CandidateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: '',
        component: CandidatePreviewComponent,
        data: {
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        resolve: {
            pagingParams: CandidatePagingParams
        }
        //  canActivate: [UserRouteAccessService]
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
    },
    {
        path: 'candidate-public-profile/:userProfile',
        component: CandidatePublicProfilePopupComponent,
        runGuardsAndResolvers: 'always',
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
