import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CollegeComponent } from './college.component';
import { CollegeDetailComponent } from './college-detail.component';
import { CollegePopupComponent } from './college-dialog.component';
import { CollegeDeletePopupComponent } from './college-delete-dialog.component';
import { Injectable } from '@angular/core';
import { RouterStateSnapshot } from '@angular/router';
import { Resolve } from '@angular/router';
import { ActivatedRouteSnapshot } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';



@Injectable()
export class CollegeResolvePagingParams implements Resolve<any> {

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


export const collegeRoute: Routes = [
    {
        path: 'college',
        component: CollegeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        resolve: {
            'pagingParams': CollegeResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'college/:id',
        component: CollegeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        resolve: {
            'pagingParams': CollegeResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    }
];

export const collegePopupRoute: Routes = [
    {
        path: 'college-new',
        component: CollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'college/:id/edit',
        component: CollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'college/:id/delete',
        component: CollegeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
