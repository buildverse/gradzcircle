import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { VisaTypeComponent } from './visa-type.component';
import { VisaTypeDetailComponent } from './visa-type-detail.component';
import { VisaTypePopupComponent } from './visa-type-dialog.component';
import { VisaTypeDeletePopupComponent } from './visa-type-delete-dialog.component';

export const visaTypeRoute: Routes = [
    {
        path: 'visa-type',
        component: VisaTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.visaType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'visa-type/:id',
        component: VisaTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.visaType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const visaTypePopupRoute: Routes = [
    {
        path: 'visa-type-new',
        component: VisaTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.visaType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'visa-type/:id/edit',
        component: VisaTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.visaType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'visa-type/:id/delete',
        component: VisaTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.visaType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
