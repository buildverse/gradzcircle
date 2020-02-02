import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { JobConstants } from './job.constants';
import { Job } from './job.model';
import { JobService } from './job.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { AuthoritiesConstants } from '../../shared/authorities.constant';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { JOB_ID, CORPORATE_ID, MATCH_SCORE, USER_DATA, USER_TYPE, BUSINESS_PLAN_ENABLED } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { AppConfigService, AppConfig } from '../app-config';
import { Corporate } from '../corporate/corporate.model';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-job',
    templateUrl: './job.component.html',
    styleUrls: ['job.css']
})
export class JobComponent implements OnInit, OnDestroy {
    jobs: Job[] = null;
    currentAccount: any;
    corporateId: number;
    eventSubscriber: Subscription;
    userLoadSubscriber: Subscription;
    currentSearch: string;
    DRAFT: number;
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
    candidateId: number;
    job: Job;
    corporate: Corporate;
    businessPlanEnabled: boolean;
    appConfigs: AppConfig[];

    constructor(
        private jobService: JobService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private dataStorageService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private appConfigService: AppConfigService
    ) {
        this.businessPlanEnabled = false;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    setAddCandidateToJobRouteData(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    loadAll() {
        if (this.corporateId) {
            this.loadActiveJobs();
        } else if (this.candidateId) {
            this.loadActiveAndMatchedJobsForCandidates();
        } else {
            if (this.currentSearch) {
                this.jobService
                    .search({
                        page: this.page - 1,
                        query: this.currentSearch,
                        size: this.itemsPerPage,
                        sort: this.sort()
                    })
                    .subscribe(
                        (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                        (res: HttpResponse<any>) => this.onError(res.body)
                    );
                return;
            }

            this.jobService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<Job[]>) => {
                        this.jobs = res.body;
                        this.currentSearch = '';
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    reset() {
        this.page = 0;
        this.jobs = [];
        if (this.corporateId) {
            this.loadActiveJobs();
        } else if (this.candidateId) {
            this.loadActiveAndMatchedJobsForCandidates();
        } else {
            this.loadAll();
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/job'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });

        this.loadAll();
    }

    loadActiveJobs() {
        this.jobService
            .queryActiveJobsByCorporate({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                id: this.corporateId
            })
            .subscribe(
                (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    setJobRouterParam() {
        this.dataStorageService.setdata(CORPORATE_ID, this.corporateId);
    }

    setJobEditOrViewRouteParam(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    setJobViewParamForCandidate(jobId, matchScore) {
        this.dataStorageService.setdata(JOB_ID, jobId);
        this.dataStorageService.setdata(MATCH_SCORE, matchScore);
    }

    setJobAndCorporateRouteParam(jobId, corporateId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
        this.dataStorageService.setdata(CORPORATE_ID, corporateId);
    }

    setParamsToGetListForAppliedCandidate(jobId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
    }

    setParamsToGetListForShortlistedCandidate(jobId, corporateId) {
        this.dataStorageService.setdata(JOB_ID, jobId);
        this.dataStorageService.setdata(CORPORATE_ID, corporateId);
    }

    ngOnInit() {
        this.corporateId = null;
        this.job = new Job();
        this.corporate = new Corporate();
        this.DRAFT = JobConstants.DRAFT;
        // console.log('biz plan in job comp is ' + this.dataStorageService.getData(BUSINESS_PLAN_ENABLED));
        this.principal.identity().then(account => {
            if (account) {
                if (this.dataStorageService.getData(USER_TYPE) === AuthoritiesConstants.CORPORATE) {
                    this.currentAccount = account;
                    this.spinnerService.show();
                    this.corporate = JSON.parse(this.dataStorageService.getData(USER_DATA));
                    this.currentSearch = this.corporate.id.toString();
                    this.corporateId = this.corporate.id;
                    this.businessPlanEnabled = JSON.parse(this.dataStorageService.getData(BUSINESS_PLAN_ENABLED));
                    //  console.log('Load active jobs');
                    this.loadActiveJobs();
                    this.registerChangeInJobs();
                } else if (this.dataStorageService.getData(USER_TYPE) === AuthoritiesConstants.ADMIN) {
                    this.jobService
                        .query({
                            page: this.page - 1,
                            size: this.itemsPerPage,
                            sort: this.sort()
                        })
                        .subscribe(
                            (res: HttpResponse<Job[]>) => {
                                this.jobs = res.body;
                                this.currentSearch = '';
                            },
                            (res: HttpErrorResponse) => this.onError(res.message)
                        );
                }
            } else {
                this.loadAll();
            }
        });
        /*  this.principal.identity(false).then((account) => {
      if (account) {
        this.loadActiveJobs();
      } else {
        this.loadAll();
      }
    });*/
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
        if (this.userLoadSubscriber) {
            this.eventManager.destroy(this.userLoadSubscriber);
        }
        if (this.jhiAlertService) {
            this.jhiAlertService.clear();
        }
    }

    trackId(index: number, item: Job) {
        return item.id;
    }

    registerChangeInJobs() {
        if (this.corporateId) {
            this.eventSubscriber = this.eventManager.subscribe('jobListModification', response => this.loadActiveJobs());
        } else if (this.candidateId) {
            this.eventSubscriber = this.eventManager.subscribe('jobListModification', response =>
                this.loadActiveAndMatchedJobsForCandidates()
            );
        } else {
            this.eventSubscriber = this.eventManager.subscribe('jobListModification', response => this.loadAll());
        }
    }

    loadActiveAndMatchedJobsForCandidates() {
        this.jobService
            .queryActiveJobsForCandidates({
                page: this.page - 1,
                // query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                id: this.candidateId
            })
            .subscribe(
                (res: HttpResponse<Job[]>) => {
                    this.onSuccess(res.body, res.headers);
                    // console.log('Loading in success is '+this.loading);
                },
                (res: HttpResponse<any>) => {
                    this.onError(res.body);
                }
            );
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
        this.jhiAlertService.error(error.message, null, null);
    }

    private onSuccess(data, headers) {
        // console.log('HEADER IS ----> ' + JSON.stringify(headers));
        // console.log('DATA IS ----> ' + JSON.stringify(data));
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.jobs = data;
        this.spinnerService.hide();
        if (this.jobs.length === 0) {
            // this.job = new Job();
            this.job.totalNumberOfJobs = 0;
            this.job.jobsLastMonth = 0;
            this.job.newApplicants = 0;
            this.job.totalLinkedCandidates = 0;
            this.jhiAlertService.addAlert({ type: 'info', msg: 'gradzcircleApp.corporate.firstLogin' }, []);
        } else {
            // this.job = new Job();
            this.job.totalNumberOfJobs = this.jobs[0].totalNumberOfJobs;
            this.job.jobsLastMonth = this.jobs[0].jobsLastMonth;
            this.job.newApplicants = this.jobs[0].newApplicants;
            this.job.totalLinkedCandidates = this.jobs[0].totalLinkedCandidates;
            this.job.corporateEscrowAmount = this.jobs[0].corporateEscrowAmount;
        }
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/job',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/job',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }
}
