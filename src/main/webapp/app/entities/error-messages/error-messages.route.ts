import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ErrorMessagesComponent } from './error-messages.component';
import { ErrorMessagesDetailComponent } from './error-messages-detail.component';
import { ErrorMessagesPopupComponent } from './error-messages-dialog.component';
import { ErrorMessagesDeletePopupComponent } from './error-messages-delete-dialog.component';

export const errorMessagesRoute: Routes = [
    {
        path: 'error-messages',
        component: ErrorMessagesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.errorMessages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'error-messages/:id',
        component: ErrorMessagesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.errorMessages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const errorMessagesPopupRoute: Routes = [
    {
        path: 'error-messages-new',
        component: ErrorMessagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.errorMessages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'error-messages/:id/edit',
        component: ErrorMessagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.errorMessages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'error-messages/:id/delete',
        component: ErrorMessagesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.errorMessages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
