import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { AppConfigComponent } from './app-config.component';
import { AppConfigDetailComponent } from './app-config-detail.component';
import { AppConfigPopupComponent } from './app-config-dialog.component';
import { AppConfigDeletePopupComponent } from './app-config-delete-dialog.component';

export const appConfigRoute: Routes = [
    {
        path: 'app-config',
        component: AppConfigComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.appConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'app-config/:id',
        component: AppConfigDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.appConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appConfigPopupRoute: Routes = [
    {
        path: 'app-config-new',
        component: AppConfigPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.appConfig.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-config/:id/edit',
        component: AppConfigPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.appConfig.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-config/:id/delete',
        component: AppConfigDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.appConfig.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
