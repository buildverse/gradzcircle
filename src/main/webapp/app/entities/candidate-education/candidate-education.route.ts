
import {Routes} from '@angular/router';
import {UserRouteAccessService} from '../../shared';
import {CandidateEducationComponent} from './candidate-education.component';
import {CandidateEducationDetailComponent} from './candidate-education-detail.component';
import {CandidateEducationPopupComponent} from './candidate-education-dialog.component';
import {CandidateEducationDeletePopupComponent} from './candidate-education-delete-dialog.component';
import {CandidateEducationPopupComponentNew} from './candidate-education-dialog.component';

export const candidateEducationRoute: Routes = [
  {
    path: 'candidate-education',
    component: CandidateEducationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gradzcircleApp.candidateEducation.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
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
    component: CandidateEducationPopupComponentNew,
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
