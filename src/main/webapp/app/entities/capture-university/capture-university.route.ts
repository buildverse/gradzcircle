import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CaptureUniversityComponent } from './capture-university.component';
import { CaptureUniversityDetailComponent } from './capture-university-detail.component';
import { CaptureUniversityPopupComponent } from './capture-university-dialog.component';
import { CaptureUniversityDeletePopupComponent } from './capture-university-delete-dialog.component';

export const captureUniversityRoute: Routes = [
    {
        path: 'capture-university',
        component: CaptureUniversityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureUniversity.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'capture-university/:id',
        component: CaptureUniversityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureUniversity.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const captureUniversityPopupRoute: Routes = [
    {
        path: 'capture-university-new',
        component: CaptureUniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureUniversity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-university/:id/edit',
        component: CaptureUniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureUniversity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-university/:id/delete',
        component: CaptureUniversityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureUniversity.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
