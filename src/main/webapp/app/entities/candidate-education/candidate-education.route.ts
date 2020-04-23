import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';
import { CandidateEducationComponent } from './candidate-education.component';
import { CandidateEducationDetailComponent } from './candidate-education-detail.component';
import { CandidateEducationPopupComponent } from './candidate-education-dialog.component';
import { CandidateEducationDeletePopupComponent } from './candidate-education-delete-dialog.component';
import { CandidateEducationPopupNewComponent } from './candidate-education-dialog.component';

export const candidateEducationRoute: Routes = [
    {
        path: '',
        component: CandidateEducationComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-education',
        component: CandidateEducationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-education/:id',
        component: CandidateEducationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateEducationPopupRoute: Routes = [
    {
        path: 'candidate-education-new',
        component: CandidateEducationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'new-candidate-education',
        component: CandidateEducationPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },

        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-education/:id/edit',
        component: CandidateEducationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },

        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-education/edit',
        component: CandidateEducationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },

        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-education/:id/delete',
        component: CandidateEducationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-education/delete',
        component: CandidateEducationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateEducation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
