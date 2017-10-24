import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SkillsComponent } from './skills.component';
import { SkillsDetailComponent } from './skills-detail.component';
import { SkillsPopupComponent } from './skills-dialog.component';
import { SkillsDeletePopupComponent } from './skills-delete-dialog.component';

export const skillsRoute: Routes = [
    {
        path: 'skills',
        component: SkillsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.skills.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'skills/:id',
        component: SkillsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.skills.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const skillsPopupRoute: Routes = [
    {
        path: 'skills-new',
        component: SkillsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.skills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'skills/:id/edit',
        component: SkillsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.skills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'skills/:id/delete',
        component: SkillsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.skills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
