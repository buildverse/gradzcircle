import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AuditComponent } from './audit.component';
import { AuditDetailComponent } from './audit-detail.component';
import { AuditPopupComponent } from './audit-dialog.component';
import { AuditDeletePopupComponent } from './audit-delete-dialog.component';

export const auditRoute: Routes = [
    {
        path: 'audit',
        component: AuditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.audit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'audit/:id',
        component: AuditDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.audit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const auditPopupRoute: Routes = [
    {
        path: 'audit-new',
        component: AuditPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.audit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'audit/:id/edit',
        component: AuditPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.audit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'audit/:id/delete',
        component: AuditDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.audit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
