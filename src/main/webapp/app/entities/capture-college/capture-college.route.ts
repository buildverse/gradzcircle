import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CaptureCollegeComponent } from './capture-college.component';
import { CaptureCollegeDetailComponent } from './capture-college-detail.component';
import { CaptureCollegePopupComponent } from './capture-college-dialog.component';
import { CaptureCollegeDeletePopupComponent } from './capture-college-delete-dialog.component';

export const captureCollegeRoute: Routes = [
    {
        path: 'capture-college',
        component: CaptureCollegeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCollege.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'capture-college/:id',
        component: CaptureCollegeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCollege.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const captureCollegePopupRoute: Routes = [
    {
        path: 'capture-college-new',
        component: CaptureCollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCollege.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-college/:id/edit',
        component: CaptureCollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCollege.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-college/:id/delete',
        component: CaptureCollegeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCollege.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
