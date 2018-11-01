import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { IndustryComponent } from './industry.component';
import { IndustryDetailComponent } from './industry-detail.component';
import { IndustryPopupComponent } from './industry-dialog.component';
import { IndustryDeletePopupComponent } from './industry-delete-dialog.component';

export const industryRoute: Routes = [
    {
        path: 'industry',
        component: IndustryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.industry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'industry/:id',
        component: IndustryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.industry.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const industryPopupRoute: Routes = [
    {
        path: 'industry-new',
        component: IndustryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.industry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'industry/:id/edit',
        component: IndustryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.industry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'industry/:id/delete',
        component: IndustryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.industry.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
