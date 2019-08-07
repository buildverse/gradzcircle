import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';
import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';
import {CandidateProfileComponent} from './candidate-profile.component';
import {CandidateProfileComponentNew} from './candidate-profile-new.component';
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
    component: CandidateProfileComponentNew,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService],
    resolve: {
      candidate: CandidateResolverService
    }
  },
  {
    path: 'education',
    component: CandidateEducationComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'employment',
    component: CandidateEmploymentComponent,
    data: {
      authorities: ['ROLE_USER', 'ROLE_CANDIDATE'],
      pageTitle: 'gradzcircleApp.candidate.home.title'
    },
    canActivate: [UserRouteAccessService]
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