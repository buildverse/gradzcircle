import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { EmploymentTypeComponent } from './employment-type.component';
import { EmploymentTypeDetailComponent } from './employment-type-detail.component';
import { EmploymentTypePopupComponent } from './employment-type-dialog.component';
import { EmploymentTypeDeletePopupComponent } from './employment-type-delete-dialog.component';

export const employmentTypeRoute: Routes = [
    {
        path: 'employment-type',
        component: EmploymentTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employmentType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'employment-type/:id',
        component: EmploymentTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employmentType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employmentTypePopupRoute: Routes = [
    {
        path: 'employment-type-new',
        component: EmploymentTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employmentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employment-type/:id/edit',
        component: EmploymentTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employmentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employment-type/:id/delete',
        component: EmploymentTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.employmentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
