import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobTypeComponent } from './job-type.component';
import { JobTypeDetailComponent } from './job-type-detail.component';
import { JobTypePopupComponent } from './job-type-dialog.component';
import { JobTypeDeletePopupComponent } from './job-type-delete-dialog.component';

export const jobTypeRoute: Routes = [
    {
        path: 'job-type',
        component: JobTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-type/:id',
        component: JobTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobTypePopupRoute: Routes = [
    {
        path: 'job-type-new',
        component: JobTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-type/:id/edit',
        component: JobTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-type/:id/delete',
        component: JobTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
