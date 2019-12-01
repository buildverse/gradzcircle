import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { CaptureQualificationComponent } from './capture-qualification.component';
import { CaptureQualificationDetailComponent } from './capture-qualification-detail.component';
import { CaptureQualificationPopupComponent } from './capture-qualification-dialog.component';
import { CaptureQualificationDeletePopupComponent } from './capture-qualification-delete-dialog.component';

export const captureQualificationRoute: Routes = [
    {
        path: 'capture-qualification',
        component: CaptureQualificationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureQualification.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'capture-qualification/:id',
        component: CaptureQualificationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureQualification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const captureQualificationPopupRoute: Routes = [
    {
        path: 'capture-qualification-new',
        component: CaptureQualificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureQualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-qualification/:id/edit',
        component: CaptureQualificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureQualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-qualification/:id/delete',
        component: CaptureQualificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureQualification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
