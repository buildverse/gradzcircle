import { Principal } from '../../core/auth/principal.service';
import { CandidateProfileSettingService } from '../../profiles/candidate/candidate-profile-setting.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Job } from '../../shared/job-common/job.model';
import { JobService } from './job.service';

import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { JobConstants } from './job.constants';
import { HttpResponse } from '@angular/common/http';
import { JOB_ID, MATCH_SCORE, USER_TYPE, USER_DATA, HAS_EDUCATION } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Candidate } from '../candidate/candidate.model';
import { Corporate } from '../corporate/corporate.model';
import { JobListEmitterService } from './job-list-change.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-job',
    templateUrl: './job-for-anonymous.component.html',
    styleUrls: ['job_new.css']
})
export class JobAnonymousComponent implements OnInit, OnDestroy {
    candidateId: number;
    currentAccount: any;
    DRAFT: number;
    corporate: Corporate;
    candidate: Candidate;
    job: Job;
    corporateId: number;
    jobs: Job[] = null;
    eventSubscriberCandidate: Subscription;
    eventSubscriberAnonymous: Subscription;
    loginEventSubscriber: Subscription;
    jobChangeSubscription: Subscription;
    userLoadSubscriber: Subscription;
    currentSearch: string;
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    totalNumberOfActiveJobs: any;
    isEmpCollapsed: boolean;
    isJobTypeCollapsed: boolean;
    isMatchScoreCollapsed: boolean;
    activeTab: string;
    countOfPermanentEmployment?: number;
    countOfContractEmployment?: number;
    countOfFullTimeJob?: number;
    countOfPartTimeJob?: number;
    countOfInternJob?: number;
    countOfSummerJob?: number;
    permanent?: boolean;
    contract?: boolean;
    fullTime?: boolean;
    partTime?: boolean;
    internship?: boolean;
    summerJob?: boolean;
    employmentType?: boolean;
    jobType?: boolean;
    matchScoreFrom?: number;
    matchScoreTo?: number;
    matchScoreRange?: string;
    navigationSubscription?: any;
    hasEducation: boolean;
    profile: string;

    constructor(
        private jobService: JobService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private principal: Principal,
        private dataStorageService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private jobListEmitterService: JobListEmitterService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.isEmpCollapsed = true;
        this.isJobTypeCollapsed = true;
        this.isMatchScoreCollapsed = true;
        this.profile = 'viewJobs';
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }
    resetForUserFilter() {
        this.page = 0;
        this.jobs = [];
    }
    reset() {
        this.page = 0;
        this.jobs = [];
        this.loadActiveJobsByUserFilter();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/viewjobs'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                // search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });

        this.loadActiveJobsByUserFilter(true);
    }

    clearMatchScores() {
        this.matchScoreRange = undefined;
        this.loadActiveJobsByUserFilter();
    }

    loadActiveJobsByUserFilter(transition?: boolean) {
        if (!transition) {
            this.resetForUserFilter();
        }
        this.setMatchScoreRange();
        if (this.permanent || this.contract) {
            this.employmentType = true;
        } else {
            this.employmentType = false;
        }
        if (this.fullTime || this.partTime || this.summerJob || this.internship) {
            this.jobType = true;
        } else {
            this.jobType = false;
        }

        if (this.contract && this.permanent && !this.jobType) {
            if (this.candidateId > 0) {
                this.loadActiveJobsForCandidate();
            } else {
                this.loadActiveJobs();
            }
        } else if (this.fullTime && this.partTime && this.internship && this.summerJob && !this.employmentType) {
            if (this.candidateId > 0) {
                this.loadActiveJobsForCandidate();
            } else {
                this.loadActiveJobs();
            }
        } else if (this.contract && !this.jobType) {
            this.loadActiveJobsByEmploymentType(JobConstants.EMPLOYMENT_TYPE_CONTRACT);
        } else if (this.permanent && !this.jobType) {
            this.loadActiveJobsByEmploymentType(JobConstants.EMPLOYMENT_TYPE_PERMANENT);
        } else if (this.contract && this.fullTime && !this.permanent && !this.partTime && !this.summerJob && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_CONTRACT, JobConstants.JOB_TYPE_FULL_TIME);
        } else if (this.contract && this.partTime && !this.permanent && !this.fullTime && !this.summerJob && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_CONTRACT, JobConstants.JOB_TYPE_PART_TIME);
        } else if (this.contract && this.internship && !this.permanent && !this.partTime && !this.fullTime && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_CONTRACT, JobConstants.JOB_TYPE_INTERNSHIP);
        } else if (this.contract && this.summerJob && !this.permanent && !this.fullTime && !this.partTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_CONTRACT, JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.permanent && this.fullTime && !this.contract && !this.summerJob && !this.partTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_PERMANENT, JobConstants.JOB_TYPE_FULL_TIME);
        } else if (this.permanent && this.partTime && !this.contract && !this.fullTime && !this.internship && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_PERMANENT, JobConstants.JOB_TYPE_PART_TIME);
        } else if (this.permanent && this.internship && !this.contract && !this.fullTime && !this.partTime && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_PERMANENT, JobConstants.JOB_TYPE_INTERNSHIP);
        } else if (this.permanent && this.summerJob && !this.contract && !this.fullTime && !this.partTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndOneJobType(JobConstants.EMPLOYMENT_TYPE_PERMANENT, JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.contract && this.fullTime && this.partTime && !this.permanent && !this.internship && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME
            );
        } else if (this.contract && this.fullTime && this.internship && !this.permanent && !this.partTime && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.contract && this.fullTime && this.summerJob && !this.permanent && !this.partTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.contract && this.partTime && this.internship && !this.permanent && !this.fullTime && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.contract && this.partTime && this.summerJob && !this.permanent && !this.fullTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.contract && this.internship && this.summerJob && !this.permanent && !this.partTime && !this.fullTime) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_INTERNSHIP,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.permanent && this.fullTime && this.partTime && !this.contract && !this.summerJob && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME
            );
        } else if (this.permanent && this.fullTime && this.internship && !this.contract && !this.partTime && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.permanent && this.fullTime && this.summerJob && !this.contract && !this.partTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.permanent && this.partTime && this.internship && !this.contract && !this.summerJob && !this.fullTime) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.permanent && this.partTime && this.summerJob && !this.contract && !this.fullTime && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.permanent && this.internship && this.summerJob && !this.contract && !this.fullTime && !this.partTime) {
            this.loadActiveJobsByEmploymentTypeAndTwoJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_INTERNSHIP,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.contract && this.fullTime && this.partTime && this.summerJob && !this.permanent && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.contract && this.fullTime && this.partTime && this.internship && !this.permanent && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.contract && this.partTime && this.summerJob && this.internship && !this.permanent && !this.fullTime) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.contract && this.summerJob && this.internship && this.fullTime && !this.permanent && !this.partTime) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_CONTRACT,
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP,
                JobConstants.JOB_TYPE_FULL_TIME
            );
        } else if (this.permanent && this.fullTime && this.partTime && this.summerJob && !this.contract && !this.internship) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.permanent && this.fullTime && this.partTime && this.internship && !this.contract && !this.summerJob) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.permanent && this.partTime && this.summerJob && this.internship && !this.contract && !this.fullTime) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.permanent && this.summerJob && this.internship && this.fullTime && !this.contract && !this.partTime) {
            this.loadActiveJobsByEmploymentTypeAndThreeJobType(
                JobConstants.EMPLOYMENT_TYPE_PERMANENT,
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP,
                JobConstants.JOB_TYPE_FULL_TIME
            );
        } else if (this.fullTime && !this.partTime && !this.summerJob && !this.internship) {
            this.loadActiveJobsByJobType(JobConstants.JOB_TYPE_FULL_TIME);
        } else if (this.partTime && !this.fullTime && !this.summerJob && !this.internship) {
            this.loadActiveJobsByJobType(JobConstants.JOB_TYPE_PART_TIME);
        } else if (this.internship && !this.partTime && !this.summerJob && !this.fullTime) {
            this.loadActiveJobsByJobType(JobConstants.JOB_TYPE_INTERNSHIP);
        } else if (this.summerJob && !this.partTime && !this.fullTime && !this.internship) {
            this.loadActiveJobsByJobType(JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.fullTime && this.partTime && !this.summerJob && !this.internship) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_FULL_TIME, JobConstants.JOB_TYPE_PART_TIME);
        } else if (this.fullTime && this.internship && !this.partTime && !this.summerJob) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_FULL_TIME, JobConstants.JOB_TYPE_INTERNSHIP);
        } else if (this.fullTime && this.summerJob && !this.partTime && !this.internship) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_FULL_TIME, JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.partTime && this.internship && !this.fullTime && !this.summerJob) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_PART_TIME, JobConstants.JOB_TYPE_INTERNSHIP);
        } else if (this.partTime && this.summerJob && !this.fullTime && !this.internship) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_PART_TIME, JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.internship && this.summerJob && !this.fullTime && !this.partTime) {
            this.loadActiveJobsByTwoJobType(JobConstants.JOB_TYPE_INTERNSHIP, JobConstants.JOB_TYPE_SUMMER_JOB);
        } else if (this.fullTime && this.partTime && this.summerJob && !this.internship) {
            this.loadActiveJobsByThreeJobType(
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB
            );
        } else if (this.fullTime && this.partTime && this.internship && !this.summerJob) {
            this.loadActiveJobsByThreeJobType(
                JobConstants.JOB_TYPE_FULL_TIME,
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.partTime && this.summerJob && this.internship && !this.fullTime) {
            this.loadActiveJobsByThreeJobType(
                JobConstants.JOB_TYPE_PART_TIME,
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP
            );
        } else if (this.summerJob && this.internship && this.fullTime && !this.partTime) {
            this.loadActiveJobsByThreeJobType(
                JobConstants.JOB_TYPE_SUMMER_JOB,
                JobConstants.JOB_TYPE_INTERNSHIP,
                JobConstants.JOB_TYPE_FULL_TIME
            );
        } else if (this.fullTime && this.partTime && this.summerJob && this.internship && this.contract && !this.permanent) {
            this.loadActiveJobsByEmploymentType(JobConstants.EMPLOYMENT_TYPE_CONTRACT);
        } else if (this.fullTime && this.partTime && this.summerJob && this.internship && !this.contract && this.permanent) {
            this.loadActiveJobsByEmploymentType(JobConstants.EMPLOYMENT_TYPE_PERMANENT);
        } else {
            if (this.candidateId > 0) {
                this.loadActiveJobsForCandidate();
            } else {
                this.loadActiveJobs();
            }
        }
    }

    setJobViewParamForCandidate(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    loadActiveJobsByEmploymentType(employmentType: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByEmploymentType(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                employmentType,
                this.candidateId > 0 ? this.candidateId : -1,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    setMatchScoreRange() {
        if (this.matchScoreRange === 'matchScore55To70') {
            this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_55;
            this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_70;
        } else if (this.matchScoreRange === 'matchScore71To84') {
            this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_71;
            this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_84;
        } else if (this.matchScoreRange === 'matchScore85To100') {
            this.matchScoreFrom = JobConstants.MATCH_SCORE_GREATER_THAN_85;
            this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_100;
        } else if (this.matchScoreRange === 'matchScoreLessThan55') {
            this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_0;
            this.matchScoreTo = JobConstants.MATCH_SCORE_LESS_THAN_55;
        } else {
            this.matchScoreFrom = -1;
            this.matchScoreTo = -1;
        }
    }

    loadActiveJobsByEmploymentTypeAndOneJobType(employmentType: string, jobType: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByEmploymentTypeAndOneJobType(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                employmentType,
                jobType,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsByEmploymentTypeAndTwoJobType(employmentType: string, jobType1: string, jobType2: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByEmploymentTypeAndTwoJobTypes(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                employmentType,
                jobType1,
                jobType2,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsByEmploymentTypeAndThreeJobType(employmentType: string, jobType1: string, jobType2: string, jobType3: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByEmploymentTypeAndThreeJobTypes(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                employmentType,
                jobType1,
                jobType2,
                jobType3,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsByJobType(jobType: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByOneJobType(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                jobType,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsByTwoJobType(jobType1: string, jobType2: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByTwoJobTypes(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                jobType1,
                jobType2,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsByThreeJobType(jobType1: string, jobType2: string, jobType3: string) {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsByThreeJobTypes(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                jobType1,
                jobType2,
                jobType3,
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobs() {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobs({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    loadActiveJobsForCandidate() {
        this.spinnerService.show();
        this.jobService
            .queryActiveJobsForCandidates(
                {
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                },
                this.candidateId,
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    setJobEditOrViewRouteParam(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    ngOnInit() {
        this.job = new Job();
        this.corporate = new Corporate();
        this.candidate = new Candidate();
        this.DRAFT = JobConstants.DRAFT;
        this.employmentType = false;
        this.jobType = false;
        this.permanent = false;
        this.contract = false;
        this.fullTime = false;
        this.partTime = false;
        this.summerJob = false;
        this.internship = false;
        this.matchScoreFrom = -1;
        this.matchScoreTo = -1;
        this.candidateId = -1;
        this.jobService.queryForActiveJobCount().subscribe(response => (this.totalNumberOfActiveJobs = response));
        this.userLoadSubscriber = this.eventManager.subscribe('userDataLoadedSuccess', response => {
            this.principal.identity().then(account => {
                if (account) {
                    this.currentAccount = account;
                    this.spinnerService.show();
                    if (this.dataStorageService.getData(USER_TYPE) === AuthoritiesConstants.CANDIDATE) {
                        this.loadCandidateInfo();
                    } else if (this.dataStorageService.getData(USER_TYPE) === AuthoritiesConstants.CORPORATE) {
                        this.loadActiveJobs();
                    } else {
                        this.loadActiveJobsByUserFilter();
                    }
                    this.registerChangeInJobs();
                } else {
                    this.loadActiveJobsByUserFilter();
                }
            });
        });

        this.principal.identity(false).then(account => {
            if (account) {
                this.loadCandidateInfo();
            } else {
                this.loadActiveJobsByUserFilter();
            }
        });
        this.jobChangeSubscription = this.jobListEmitterService.currentMessage.subscribe(jobChangeEvent => {
            if (jobChangeEvent) {
                this.loadActiveJobsByUserFilter();
            }
        });
    }

    loadCandidateInfo() {
        this.jhiAlertService.clear();
        this.candidate = JSON.parse(this.dataStorageService.getData(USER_DATA));
        this.hasEducation = JSON.parse(this.dataStorageService.getData(HAS_EDUCATION));
        this.candidateId = this.candidate.id;
        if (this.candidate.profileScore <= 20 && !this.hasEducation) {
            this.jhiAlertService.addAlert({ type: 'info', msg: 'gradzcircleApp.candidate.profile.profileAlert', timeout: 5000 }, []);
        }
        if (!this.hasEducation) {
            this.jhiAlertService.addAlert({ type: 'info', msg: 'gradzcircleApp.candidate.profile.educationAlert', timeout: 5000 }, []);
        }
        this.loadActiveJobsByUserFilter();
        this.spinnerService.hide();
    }

    ngOnDestroy() {
        if (this.eventSubscriberCandidate) {
            this.eventManager.destroy(this.eventSubscriberCandidate);
        }
        if (this.eventSubscriberAnonymous) {
            this.eventManager.destroy(this.eventSubscriberAnonymous);
        }
        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
        if (this.routeData) {
            this.eventManager.destroy(this.routeData);
        }
        if (this.loginEventSubscriber) {
            this.eventManager.destroy(this.loginEventSubscriber);
        }
        if (this.userLoadSubscriber) {
            this.eventManager.destroy(this.userLoadSubscriber);
        }
        this.jhiAlertService.clear();
    }

    trackId(index: number, item: Job) {
        return item.id;
    }

    registerChangeInJobs() {
        if (this.candidateId > 0) {
            this.eventSubscriberCandidate = this.eventManager.subscribe('jobListModification', response =>
                this.loadActiveJobsByUserFilter()
            );
        } else {
            this.eventSubscriberAnonymous = this.eventManager.subscribe('jobListModification', response =>
                this.loadActiveJobsByUserFilter()
            );
        }
    }

    setJobViewParamForGuest(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onError(error) {
        this.spinnerService.hide();
        this.router.navigate(['/error']);
        this.jhiAlertService.error(error.message, null, null);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.jobs = data;
        if (this.jobs && this.jobs.length > 0) {
            this.countOfPermanentEmployment = this.jobs[0].countOfPermanentEmployment;
            this.countOfContractEmployment = this.jobs[0].countOfContractEmployment;
            this.countOfFullTimeJob = this.jobs[0].countOfFullTimeJob;
            this.countOfPartTimeJob = this.jobs[0].countOfPartTimeJob;
            this.countOfInternJob = this.jobs[0].countOfInternJob;
            this.countOfSummerJob = this.jobs[0].countOfSummerJob;
        }
        this.spinnerService.hide();
    }
}
