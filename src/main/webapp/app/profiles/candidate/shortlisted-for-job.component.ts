import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import { DataService, DataStorageService} from '../../shared';
import {JobService} from '../../entities/job/job.service';
import {Job} from '../../entities/job/job.model';
import {ITEMS_PER_PAGE, Principal} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import { USER_ID } from '../../shared/constants/storage.constants';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-shortlisted-job-list',
  templateUrl: './shortlisted-for-job.component.html'
})
export class ShortListedJobsForCandidateComponent implements OnInit, OnDestroy {
  jobs: Job[];
  data: any;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: number;
  predicate: any;
  previousPage: any;
  reverse: any;
  candidateId: number;
  corporateId:number;
  eventSubscriber: Subscription;
  subscription: Subscription;

  constructor(
    private jobService: JobService,
    private jhiAlertService: JhiAlertService,
    private activatedRoute: ActivatedRoute,
    private parseLinks: JhiParseLinks,
    private router: Router,
    private dataService: DataService,
    private localDataStorageService: DataStorageService

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
   // this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
  }

 

  reset() {
    this.page = 0;
    this.jobs = [];
    if (this.candidateId) {
      this.loadShortListedJobs();
    } 
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/shortListedForJob'], {
      queryParams:
      {
        page: this.page,
        size: this.itemsPerPage,
       // search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });

   this.loadShortListedJobs();
  }

  loadShortListedJobs() {
    this.jobService.queryShortListedJobsForCandidate({
      page: this.page - 1,
      //query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.candidateId
    }).subscribe(
      (res: HttpResponse<Job[]>) => this.onSuccess(res.body, res.headers),
      (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {

      if (params['id']) {
        this.candidateId = params['id'];
      } else {
        this.candidateId = this.dataService.getRouteData();
      } 
      
      if(!this.candidateId) {
        this.candidateId = this.localDataStorageService.getData(USER_ID);
      }
      this.loadShortListedJobs();
    });


  }

  ngOnDestroy() {
   // this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: Job) {
    return item.id;
  }
  
 

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onError(error) {
    console.log(error);
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
  }
}
