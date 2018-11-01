import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { FilterCategoryComponent } from './filter-category.component';
import { FilterCategoryDetailComponent } from './filter-category-detail.component';
import { FilterCategoryPopupComponent } from './filter-category-dialog.component';
import { FilterCategoryDeletePopupComponent } from './filter-category-delete-dialog.component';

export const filterCategoryRoute: Routes = [
    {
        path: 'filter-category',
        component: FilterCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filterCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'filter-category/:id',
        component: FilterCategoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filterCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const filterCategoryPopupRoute: Routes = [
    {
        path: 'filter-category-new',
        component: FilterCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filterCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'filter-category/:id/edit',
        component: FilterCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filterCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'filter-category/:id/delete',
        component: FilterCategoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.filterCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
