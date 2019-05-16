import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';
import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';
import {CandidateProfileComponent} from './candidate-profile.component';
import {CandidateProfileAboutMeEditComponent} from './candidate-about-me-edit.component';
import {CandidateResolverService} from './candidate-profile-account-resolver.service';
import {CandidateCareerInterestResolverService} from './candidate-profile-career-interest-resolver.service';
import {CandidateProfileAboutMeDetailsComponent} from './candidate-about-me-details.component'
import {CandidateMaritalStatusResolverService} from './candidate-profile-marital-status-resolver.service';
import {CandidateGenderResolverService} from './candidate-profile-gender-resolver.service';
import {CandidateAboutMeEditGuard} from './candidate-profile-about-me-edit-guard';
import {CandidateDetailResolverService} from './candidate-detail-resolver.service';
import {CandidateLanguageProficiencyComponent} from '../../entities/candidate-language-proficiency/candidate-language-proficiency.component';
import {CandidateEducationComponent} from '../../entities/candidate-education/candidate-education.component';
import {CandidateEmploymentComponent} from '../../entities/candidate-employment/candidate-employment.component';
import {CandidateCertificationComponent} from '../../entities/candidate-certification/candidate-certification.component';
import {CandidateNonAcademicWorkComponent} from '../../entities/candidate-non-academic-work/candidate-non-academic-work.component';
import {CandidatePublicProfilePopupComponent} from './candidate-public-profile-popup.component';
import {AppliedJobsComponent} from './applied-job-by-candidate.component';
import {SideMenuComponent} from '../../account/side-menu/side-menu.component';
import {ShortListedJobsForCandidateComponent} from './shortlisted-for-job.component';
import {ProfilePicMgmtPopupComponent} from './profile-pic-mgmt-popup.component';

@Injectable()
export class JobResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: JhiPaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const candidateProfileRoutes: Routes = [
  {
    path: 'candidate-profile',
    component: CandidateProfileComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    resolve: {
      candidate: CandidateResolverService
    },
    canActivate: [UserRouteAccessService],

    children: [
    /*  {
        path: '',
        component: SideMenuComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'sidemenu'
      },*/
      {
        path: 'details',
        component: CandidateProfileAboutMeDetailsComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },

        canActivate: [UserRouteAccessService],
        resolve: {
          candidate: CandidateDetailResolverService
          //candidateLanguageProficiency: CandidateLanguageProficiencyResolverService
        },
        outlet: 'detail'

      },
      {
        //path: 'edit/:id',
        path:'edit',
        component: CandidateProfileAboutMeEditComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },

        canActivate: [UserRouteAccessService],
        //canDeactivate: [CandidateAboutMeEditGuard],
        resolve: {
          candidate: CandidateDetailResolverService,
          jobCategory: CandidateCareerInterestResolverService,
          maritalStatus: CandidateMaritalStatusResolverService,
          gender: CandidateGenderResolverService,
        },
         outlet: 'detail'
      },

      {
        path: 'education',
        component: CandidateEducationComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'educations'
      },
      {
        path: 'employment',
        component: CandidateEmploymentComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'employments'
      },
      {
        path: 'certification',
        component: CandidateCertificationComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'certifications'
      },
      {
        path: 'beyondAcademics',
        component: CandidateNonAcademicWorkComponent,
        data: {
          authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
          pageTitle: 'gradzcircleApp.candidate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'nonAcademics'
      },
      {
                path: 'language',
                component: CandidateLanguageProficiencyComponent,
                data: {
                    authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
                    pageTitle: 'gradzcircleApp.candidate.home.title'
                },

                canActivate: [UserRouteAccessService],
                outlet : 'languages'
 
      }
    ]
  },
  {
    path: 'appliedJobs',
    component: AppliedJobsComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    resolve: {
      'pagingParams': JobResolvePagingParams
    },
  },
  {
    path: 'shortListedForJob',
    component: ShortListedJobsForCandidateComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    resolve: {
      'pagingParams': JobResolvePagingParams
    },
  }
  
];

export const candidateProfilePopupRoute: Routes = [
  {
    path: 'candidate-public-profile',
    component: CandidatePublicProfilePopupComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE', 'ROLE_CORPORATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'update-picture',
    component: ProfilePicMgmtPopupComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE', 'ROLE_CORPORATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }

]