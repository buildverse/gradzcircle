import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
//import { matchTrackerRoute } from '../match-tracker/';
import { JobComponent } from './job.component';
import { JobDetailComponent } from './job-detail.component';
import { JobPopupComponent,JobPopupComponentNew } from './job-dialog.component';
import { JobViewComponent,JobViewPopupComponent} from './job-view.component';
import { JobRemoveDialogComponent,JobRemovePopupComponent} from './job-remove-dialog.component'
import { JobDeletePopupComponent } from './job-delete-dialog.component';
import {JobEditMessageDialogComponent,JobEditMessagePopupComponent} from './job-edit-message-dialog.component';

const TRACKER_ROUTE = [
   //matchTrackerRoute
];


export const jobRoute: Routes = [
    {
        path: 'job',
        component: JobComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job/:id',
        component: JobDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
 // matchTrackerRoute
];

export const jobPopupRoute: Routes = [
    {
        path: 'job-new',
        component: JobPopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-new/:id',
        component: JobPopupComponentNew,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/:id/edit',
        component: JobPopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/:id/view',
        component: JobViewPopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job/:id/delete',
        component: JobDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
     {
        path: 'job/:id/remove',
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
            authorities: ['ROLE_USER','ROLE_CORPORATE'],
            pageTitle: 'gradzcircleApp.job.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
