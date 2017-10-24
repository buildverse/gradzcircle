import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { QualificationComponent } from './qualification.component';
import { QualificationDetailComponent } from './qualification-detail.component';
import { QualificationPopupComponent } from './qualification-dialog.component';
import { QualificationDeletePopupComponent } from './qualification-delete-dialog.component';

export const qualificationRoute: Routes = [
    {
        path: 'qualification',
        component: QualificationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.qualification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'qualification/:id',
        component: QualificationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.qualification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const qualificationPopupRoute: Routes = [
    {
        path: 'qualification-new',
        component: QualificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.qualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'qualification/:id/edit',
        component: QualificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.qualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'qualification/:id/delete',
        component: QualificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.qualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
