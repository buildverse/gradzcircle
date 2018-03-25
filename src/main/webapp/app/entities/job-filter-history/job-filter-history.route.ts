import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobFilterHistoryComponent } from './job-filter-history.component';
import { JobFilterHistoryDetailComponent } from './job-filter-history-detail.component';
import { JobFilterHistoryPopupComponent } from './job-filter-history-dialog.component';
import { JobFilterHistoryDeletePopupComponent } from './job-filter-history-delete-dialog.component';

export const jobFilterHistoryRoute: Routes = [
    {
        path: 'job-filter-history',
        component: JobFilterHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilterHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-filter-history/:id',
        component: JobFilterHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilterHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobFilterHistoryPopupRoute: Routes = [
    {
        path: 'job-filter-history-new',
        component: JobFilterHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilterHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-filter-history/:id/edit',
        component: JobFilterHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilterHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-filter-history/:id/delete',
        component: JobFilterHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilterHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
