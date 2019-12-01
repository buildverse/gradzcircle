import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';
import { NationalityComponent } from './nationality.component';
import { NationalityDetailComponent } from './nationality-detail.component';
import { NationalityPopupComponent } from './nationality-dialog.component';
import { NationalityDeletePopupComponent } from './nationality-delete-dialog.component';

export const nationalityRoute: Routes = [
    {
        path: 'nationality',
        component: NationalityComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.nationality.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'nationality/:id',
        component: NationalityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.nationality.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const nationalityPopupRoute: Routes = [
    {
        path: 'nationality-new',
        component: NationalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nationality/:id/edit',
        component: NationalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nationality/:id/delete',
        component: NationalityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
