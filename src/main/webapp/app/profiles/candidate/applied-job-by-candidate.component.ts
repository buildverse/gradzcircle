import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobService} from '../../entities/job/job.service';
import {Job} from '../../entities/job/job.model';
import {ITEMS_PER_PAGE, Principal} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-applied-job-list',
  templateUrl: './applied-job-by-candidate.component.html'
})
export class AppliedJobsComponent implements OnInit, OnDestroy {
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
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private principal: Principal,
    private parseLinks: JhiParseLinks,
    private router: Router,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig,
    

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
      this.loadAppliedJobs();
    } 
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/appliedJobs/:id'], {
      queryParams:
      {
        page: this.page,
        size: this.itemsPerPage,
       // search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });

   this.loadAppliedJobs();
  }

  loadAppliedJobs() {
    this.jobService.queryAppliedJobsByCandidate({
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
      this.candidateId = params['id'];
      this.loadAppliedJobs(); 
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
