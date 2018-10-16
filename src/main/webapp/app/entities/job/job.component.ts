import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobConstants} from './job.constants';
import {Job} from './job.model';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {CandidateService} from '../candidate/candidate.service';
import {CorporateService} from '../corporate/corporate.service';

@Component({
  selector: 'jhi-job',
  templateUrl: './job.component.html'
})
export class JobComponent implements OnInit, OnDestroy {
  jobs: Job[];
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
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
    this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
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
          (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
          (res: ResponseWrapper) => this.onError(res.json)
          );
        return;
      }

      this.jobService.query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      }).subscribe(
        (res: ResponseWrapper) => {
          this.onSuccess(res.json, res.headers),
            this.currentSearch = '';
        },
        (res: ResponseWrapper) => this.onError(res.json)
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
    this.jobService.queryActiveJobs({
      page: this.page - 1,
      query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.corporateId
    }).subscribe(
      (res: ResponseWrapper) => {
        this.onSuccess(res.json, res.headers)
        this.currentSearch = '';
      },
      (res: ResponseWrapper) => this.onError(res.json)
      );
  }

  ngOnInit() {
    this.corporateId = null;
    this.DRAFT = JobConstants.DRAFT;
    this.principal.identity().then((account) => {
      this.currentAccount = account;
      if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
        this.corporateService.findCorporateByLoginId(account.id).subscribe((response) => {
          this.corporateId = response.id;
          this.loadActiveJobs();
        });
      } else if (account.authorities.indexOf(AuthoritiesConstants.CANDIDATE) > -1) {
        this.candidateService.getCandidateByLoginId(account.id).subscribe((response) => {
          this.candidateId = response.id;
          this.loadActiveAndMatchedJobsForCandidates();
        });
      } else {
        this.loadAll();
      }
      this.registerChangeInJobs();
      /* this.corporateService.findCorporateByLoginId(account.id).subscribe((response) => {
         this.corporateId = response.id;
         this.registerChangeInJobs();
         if (account.authorities.indexOf(AuthoritiesConstants.CORPORATE) > -1) {
           this.loadActiveJobs();
         } else {
           console.log('Am Admin yes!!!!!!!');
           this.loadAll();
           this.registerChangeInJobs();
         }
       });*/
    });
    // this.registerChangeInJobs();

  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: Job) {
    return item.id;
  }
  registerChangeInJobs() {
    console.log(' have corporate'+this.corporateId);
    console.log(' have candidate'+this.candidateId);
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
      (res: ResponseWrapper) => {
        this.onSuccess(res.json, res.headers)
        this.currentSearch = '';
      },
      (res: ResponseWrapper) => this.onError(res.json)
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
