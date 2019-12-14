import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';
import { CandidateCertificationComponent } from './candidate-certification.component';
import { CandidateCertificationDetailComponent } from './candidate-certification-detail.component';
import { CandidateCertificationPopupComponent, CandidateCertificationPopupNewComponent } from './candidate-certification-dialog.component';
import { CandidateCertificationDeletePopupComponent } from './candidate-certification-delete-dialog.component';

export const candidateCertificationRoute: Routes = [
    {
        path: 'candidate-certification',
        component: CandidateCertificationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-certification/:id',
        component: CandidateCertificationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateCertificationPopupRoute: Routes = [
    {
        path: 'candidate-certification-new',
        component: CandidateCertificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'new-candidate-certification',
        component: CandidateCertificationPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEmployment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-certification/:id/edit',
        component: CandidateCertificationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-certification/:id/delete',
        component: CandidateCertificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-certification/edit',
        component: CandidateCertificationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-certification/delete',
        component: CandidateCertificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateCertification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
