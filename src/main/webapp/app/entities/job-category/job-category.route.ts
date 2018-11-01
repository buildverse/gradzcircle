import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JobCategoryComponent } from './job-category.component';
import { JobCategoryDetailComponent } from './job-category-detail.component';
import { JobCategoryPopupComponent } from './job-category-dialog.component';
import { JobCategoryDeletePopupComponent } from './job-category-delete-dialog.component';

export const jobCategoryRoute: Routes = [
    {
        path: 'job-category',
        component: JobCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-category/:id',
        component: JobCategoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobCategoryPopupRoute: Routes = [
    {
        path: 'job-category-new',
        component: JobCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-category/:id/edit',
        component: JobCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-category/:id/delete',
        component: JobCategoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.jobCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
