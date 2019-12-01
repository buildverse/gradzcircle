import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { EmployabilityComponent } from './employability.component';
import { EmployabilityDetailComponent } from './employability-detail.component';
import { EmployabilityPopupComponent } from './employability-dialog.component';
import { EmployabilityDeletePopupComponent } from './employability-delete-dialog.component';

export const employabilityRoute: Routes = [
    {
        path: 'employability',
        component: EmployabilityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employability.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'employability/:id',
        component: EmployabilityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employability.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employabilityPopupRoute: Routes = [
    {
        path: 'employability-new',
        component: EmployabilityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employability.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employability/:id/edit',
        component: EmployabilityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employability.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employability/:id/delete',
        component: EmployabilityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employability.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
