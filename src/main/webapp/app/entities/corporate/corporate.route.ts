import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { LinkedCandidatesComponent } from './corporate-linked-candidates.component';
import { CorporateComponent } from './corporate.component';
import { CorporateDetailComponent } from './corporate-detail.component';
import { CorporatePopupComponent } from './corporate-dialog.component';
import { CorporateDeletePopupComponent } from './corporate-delete-dialog.component';

@Injectable()
export class LinkedCandidatesResolvePagingParams implements Resolve<any> {
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

export const corporateRoute: Routes = [
    {
        path: 'corporate',
        component: CorporateComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.corporate.detail.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'corporateDetails',
        component: CorporateDetailComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.corporate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'linkedCandidatesForCorporate',
        component: LinkedCandidatesComponent,
        resolve: {
            pagingParams: LinkedCandidatesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.corporate.linkedCandidates'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const corporatePopupRoute: Routes = [
    {
        path: 'corporate-new',
        component: CorporatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.corporate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'corporate/edit',
        component: CorporatePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.corporate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'corporate/:id/delete',
        component: CorporateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.corporate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
