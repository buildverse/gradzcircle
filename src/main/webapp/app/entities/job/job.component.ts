import {Component, OnInit, OnDestroy,ViewChild} from '@angular/core';
import {ActivatedRoute, Router,Event, NavigationCancel,NavigationEnd,NavigationStart} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobConstants} from './job.constants';
import {Job} from './job.model';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, Principal, DataService, DataStorageService} from '../../shared';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {JOB_ID, CORPORATE_ID, MATCH_SCORE} from '../../shared/constants/storage.constants';
import {CandidateService} from '../candidate/candidate.service';
import {CorporateService} from '../corporate/corporate.service';
import {Corporate} from '../corporate/corporate.model';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'jhi-job',
  templateUrl: './job.component.html',
  styleUrls :['job.css']
})
export class JobComponent implements OnInit, OnDestroy {
  jobs: Job[] = null;
  currentAccount: any;
  corporateId: number;
  eventSubscriber: Subscription;
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


  constructor(
    private jobService: JobService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private parseLinks: JhiParseLinks,
    private candidateService: CandidateService,
    private corporateService: CorporateService,
    private router: Router,
    private dataService: DataService,
    private dataStorageService : DataStorageService,
    private spinnerService: NgxSpinnerService

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
     this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
  }

  
  loadAll() {
    if (this.corporateId) {
      this.loadActiveJobs();
    } else if(this.candidateId) {
      this.loadActiveAndMatchedJobsForCandidates();
    } else {
      if (this.currentSearch) {
        this.jobService.search({
          page: this.page - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        }).subscribe(
           (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
           (res: HttpResponse<any>) => this.onError(res.body)
          );
        return;
      }

      this.jobService.query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      }).subscribe(
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
    } else if(this.candidateId) {
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
      queryParams:
      {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });

    this.loadAll();
  }

  loadActiveJobs() {
    this.jobService.queryActiveJobsByCorporate({
      page: this.page - 1,
      query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.corporateId
    }).subscribe(
      (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
       (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  setJobRouterParam() {
    this.dataService.setRouteData(this.corporateId);
  }
  
  setJobEditOrViewRouteParam(jobId) {
    this.dataService.setRouteData(jobId);
  }
  
  setJobViewParamForCandidate(jobId,matchScore) {
    this.dataService.put(JOB_ID,jobId);
    this.dataService.put(MATCH_SCORE,matchScore);
  }
  
  setJobAndCorporateRouteParam(jobId,corporateId) {
    this.dataService.put(JOB_ID,jobId);
    this.dataService.put(CORPORATE_ID,corporateId);
    this.dataStorageService.setdata(JOB_ID, jobId);
    this.dataStorageService.setdata(CORPORATE_ID, corporateId); 
  }
  
  
  ngOnInit() {
    this.corporateId = null;
    this.job = new Job();
    this.corporate = new Corporate();
    this.DRAFT = JobConstants.DRAFT;
    this.principal.identity().then((account) => {
      if(account) {
        this.currentAccount = account;
        if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
          this.spinnerService.show();
          this.corporateService.findCorporateByLoginId(account.id).subscribe((response) => {
            this.corporateId = response.body.id;
            this.corporate = response.body;
            this.loadActiveJobs();
          });
        } else if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
          this.spinnerService.show();
          this.candidateService.getCandidateByLoginId(account.id).subscribe((response) => {
            this.candidateId = response.body.id;
            this.loadActiveAndMatchedJobsForCandidates();
          });
        } else {
          this.loadAll();
        }
        this.registerChangeInJobs();
        } else {
        this.loadAll();
      }
    });

  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }


  trackId(index: number, item: Job) {
    return item.id;
  }
  
  registerChangeInJobs() {
    if (this.corporateId) {
      this.eventSubscriber = this.eventManager.subscribe('jobListModification', (response) => this.loadActiveJobs());
    } else if (this.candidateId) {
      this.eventSubscriber = this.eventManager.subscribe('jobListModification', (response) => this.loadActiveAndMatchedJobsForCandidates());
    } else {
      this.eventSubscriber = this.eventManager.subscribe('jobListModification', (response) => this.loadAll());
    }
  }

  loadActiveAndMatchedJobsForCandidates() {
    
    this.jobService.queryActiveJobsForCandidates({
      page: this.page - 1,
      //query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.candidateId
    }).subscribe(
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
    if (this.jobs.length === 0)    {
      //this.job = new Job();
      this.job.totalNumberOfJobs = 0;
      this.job.jobsLastMonth = 0;
      this.job.newApplicants = 0;
      this.job.totalLinkedCandidates = 0;
    } else {
     // this.job = new Job();
      this.job.totalNumberOfJobs = this.jobs[0].totalNumberOfJobs;
      this.job.jobsLastMonth = this.jobs[0].jobsLastMonth;
      this.job.newApplicants = this.jobs[0].newApplicants;
      this.job.totalLinkedCandidates = this.jobs[0].totalLinkedCandidates;
    }
  }
}
