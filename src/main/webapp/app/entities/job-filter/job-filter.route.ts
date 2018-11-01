import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JobFilterComponent } from './job-filter.component';
import { JobFilterDetailComponent } from './job-filter-detail.component';
import { JobFilterPopupComponent } from './job-filter-dialog.component';
import { JobFilterDeletePopupComponent } from './job-filter-delete-dialog.component';

export const jobFilterRoute: Routes = [
    {
        path: 'job-filter',
        component: JobFilterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-filter/:id',
        component: JobFilterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobFilterPopupRoute: Routes = [
    {
        path: 'job-filter-new',
        component: JobFilterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-filter/:id/edit',
        component: JobFilterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-filter/:id/delete',
        component: JobFilterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobFilter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
