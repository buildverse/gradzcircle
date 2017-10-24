import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MaritalStatusComponent } from './marital-status.component';
import { MaritalStatusDetailComponent } from './marital-status-detail.component';
import { MaritalStatusPopupComponent } from './marital-status-dialog.component';
import { MaritalStatusDeletePopupComponent } from './marital-status-delete-dialog.component';

export const maritalStatusRoute: Routes = [
    {
        path: 'marital-status',
        component: MaritalStatusComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.maritalStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'marital-status/:id',
        component: MaritalStatusDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.maritalStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const maritalStatusPopupRoute: Routes = [
    {
        path: 'marital-status-new',
        component: MaritalStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.maritalStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marital-status/:id/edit',
        component: MaritalStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.maritalStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marital-status/:id/delete',
        component: MaritalStatusDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.maritalStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
