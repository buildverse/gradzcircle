import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';

import { ProfileCategoryComponent } from './profile-category.component';
import { ProfileCategoryDetailComponent } from './profile-category-detail.component';
import { ProfileCategoryPopupComponent } from './profile-category-dialog.component';
import { ProfileCategoryDeletePopupComponent } from './profile-category-delete-dialog.component';

export const profileCategoryRoute: Routes = [
    {
        path: 'profile-category',
        component: ProfileCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.profileCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'profile-category/:id',
        component: ProfileCategoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.profileCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const profileCategoryPopupRoute: Routes = [
    {
        path: 'profile-category-new',
        component: ProfileCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.profileCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profile-category/:id/edit',
        component: ProfileCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.profileCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profile-category/:id/delete',
        component: ProfileCategoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.profileCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
