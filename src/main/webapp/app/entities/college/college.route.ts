import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CollegeComponent } from './college.component';
import { CollegeDetailComponent } from './college-detail.component';
import { CollegePopupComponent } from './college-dialog.component';
import { CollegeDeletePopupComponent } from './college-delete-dialog.component';

export const collegeRoute: Routes = [
    {
        path: 'college',
        component: CollegeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'college/:id',
        component: CollegeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const collegePopupRoute: Routes = [
    {
        path: 'college-new',
        component: CollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'college/:id/edit',
        component: CollegePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'college/:id/delete',
        component: CollegeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.college.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
