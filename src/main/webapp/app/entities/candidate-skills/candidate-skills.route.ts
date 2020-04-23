import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { Routes } from '@angular/router';
import { CandidateCurrentSkillsResolverService } from './candidate-current-skills.resolver.service';
import { CandidateSkillsComponent } from './candidate-skills.component';
import { CandidateSkillsDetailComponent } from './candidate-skills-detail.component';
import { CandidateSkillsPopupComponent, CandidateSkillsPopupNewComponent } from './candidate-skills-dialog.component';
import { CandidateSkillsDeletePopupComponent } from './candidate-skills-delete-dialog.component';

export const candidateSkillsRoute: Routes = [
    {
        path: '',
        component: CandidateSkillsComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidate.home.title'
        },

        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-skills',
        component: CandidateSkillsComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'candidate-skills/:id',
        component: CandidateSkillsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const candidateSkillsPopupRoute: Routes = [
    {
        path: 'candidate-skills-new',
        component: CandidateSkillsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'new-candidate-skill',
        component: CandidateSkillsPopupNewComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'radzcircleApp.candidateSkills.home.title'
        },
        /* resolve: {
      currentSkills: CandidateCurrentSkillsResolverService
    },*/
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-skills/:id/edit',
        component: CandidateSkillsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-skills/:id/delete',
        component: CandidateSkillsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'candidate-skills/delete',
        component: CandidateSkillsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
            pageTitle: 'gradzcircleApp.candidateSkills.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
