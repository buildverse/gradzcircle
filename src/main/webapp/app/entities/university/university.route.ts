import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { UniversityComponent } from './university.component';
import { UniversityDetailComponent } from './university-detail.component';
import { UniversityPopupComponent } from './university-dialog.component';
import { UniversityDeletePopupComponent } from './university-delete-dialog.component';

export const universityRoute: Routes = [
    {
        path: 'university',
        component: UniversityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.university.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'university/:id',
        component: UniversityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.university.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const universityPopupRoute: Routes = [
    {
        path: 'university-new',
        component: UniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'university/:id/edit',
        component: UniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'university/:id/delete',
        component: UniversityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
