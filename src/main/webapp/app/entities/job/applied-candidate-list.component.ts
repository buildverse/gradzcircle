import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import {CandidateList} from './candidate-list.model';

@Component({
  selector: 'jhi-applied-candidate-list',
  templateUrl: './applied-candidate-list.component.html'
})
export class AppliedCandidateListComponent implements OnInit, OnDestroy {
  candidateList: CandidateList[];
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
  jobId: number;
  score: number;
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
    this.candidateList = [];
    if (this.jobId) {
      this.loadMatchedCandidates();
    }
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/appliedCandidateList/:id'], {
      queryParams:
      {
        page: this.page,
        size: this.itemsPerPage,
        // search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });

    this.loadMatchedCandidates();
  }

  loadMatchedCandidates() {
    this.jobService.queryAppliedCandidatesForJob({
      page: this.page - 1,
      //query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.jobId,
      score: this.score
    }).subscribe(
      (res: ResponseWrapper) => {
        this.onSuccess(res.json, res.headers)
        //this.currentSearch = '';
      },
      (res: ResponseWrapper) => this.onError(res.json)
      );
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.jobId = params['id'];
      this.loadMatchedCandidates();
    });


  }

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: CandidateList) {
    return item.id;
  }

  registerChangeInMatches() {
    //  this.eventSubscriber = this.eventManager.subscribe('matchedListModification', (response) => this.loadMatchedCandidates());
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
    this.candidateList = data;
  }
}
