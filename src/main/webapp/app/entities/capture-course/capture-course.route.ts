import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CaptureCourseComponent } from './capture-course.component';
import { CaptureCourseDetailComponent } from './capture-course-detail.component';
import { CaptureCoursePopupComponent } from './capture-course-dialog.component';
import { CaptureCourseDeletePopupComponent } from './capture-course-delete-dialog.component';

export const captureCourseRoute: Routes = [
    {
        path: 'capture-course',
        component: CaptureCourseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'capture-course/:id',
        component: CaptureCourseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const captureCoursePopupRoute: Routes = [
    {
        path: 'capture-course-new',
        component: CaptureCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-course/:id/edit',
        component: CaptureCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'capture-course/:id/delete',
        component: CaptureCourseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.captureCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
