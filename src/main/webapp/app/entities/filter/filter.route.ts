import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { FilterComponent } from './filter.component';
import { FilterDetailComponent } from './filter-detail.component';
import { FilterPopupComponent } from './filter-dialog.component';
import { FilterDeletePopupComponent } from './filter-delete-dialog.component';

export const filterRoute: Routes = [
    {
        path: 'filter',
        component: FilterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filter.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'filter/:id',
        component: FilterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const filterPopupRoute: Routes = [
    {
        path: 'filter-new',
        component: FilterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'filter/:id/edit',
        component: FilterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'filter/:id/delete',
        component: FilterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
