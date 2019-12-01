import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StatesComponent } from './states.component';
import { StatesDetailComponent } from './states-detail.component';
import { StatesPopupComponent } from './states-dialog.component';
import { StatesDeletePopupComponent } from './states-delete-dialog.component';

@Injectable()
export class StatesResolvePagingParams implements Resolve<any> {
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

export const statesRoute: Routes = [
    {
        path: 'states',
        component: StatesComponent,
        resolve: {
            pagingParams: StatesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.states.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'states/:id',
        component: StatesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.states.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const statesPopupRoute: Routes = [
    {
        path: 'states-new',
        component: StatesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.states.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'states/:id/edit',
        component: StatesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.states.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'states/:id/delete',
        component: StatesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.states.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
