import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, UserService, DataService,DataStorageService} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {AuthoritiesConstants} from '../../shared/authorities.constant';
import { JOB_ID, CORPORATE_ID, CANDIDATE_ID } from '../../shared/constants/storage.constants';
import {CandidateList} from './candidate-list.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-matched-candidate-list',
  templateUrl: './matched-candidate-list.html',
  styleUrls:['job.css']
})
export class MatchedCandidateListComponent implements OnInit, OnDestroy {
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
  corporateId:number;
  eventSubscriber: Subscription;
  subscription: Subscription;
  defaultImage = require('../../../content/images/no-image.png');

  constructor(
    private jobService: JobService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private parseLinks: JhiParseLinks,
    private router: Router,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig,
    private dataService: DataService,
    private dataStorageService: DataStorageService
    

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
    this.router.navigate(['/matchedCandidateList'], {
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
    this.jobService.queryMatchedCandidatesForJob({
      page: this.page - 1,
      //query: this.currentSearch,
      size: this.itemsPerPage,
      sort: this.sort(),
      id: this.jobId
    }).subscribe(
      (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
       (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {

      if (params['id'] && params['corporateId']) {
        this.jobId = params['id'];
        this.corporateId = params['corporateId'];
      } else {
        this.jobId = parseFloat(this.dataService.get(JOB_ID));
        this.corporateId = parseFloat(this.dataService.get(CORPORATE_ID));
      }
      if (!(this.jobId || this.corporateId)) {
        this.jobId = this.dataStorageService.getData(JOB_ID);
        this.corporateId = this.dataStorageService.getData(CORPORATE_ID);
      }
      this.loadMatchedCandidates();
      this.registerChangeInMatches();
    });


  }
  
  setViewPublicProfileRouteParams(candidateId, jobId, corporateId) {
    this.dataService.put(CANDIDATE_ID,candidateId);
    this.dataService.put(JOB_ID,jobId);
    this.dataService.put(CORPORATE_ID,corporateId);
  }

  ngOnDestroy() {
   // this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: CandidateList) {
    return item.id;
  }
  
  registerChangeInMatches() {
    this.eventSubscriber = this.eventManager.subscribe('matchedListModification', (response) => this.loadMatchedCandidates());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onError(error) {
   // console.log(error);
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
    this.setImageUrl();
  }
  
  private setImageUrl() {
    this.candidateList.forEach((candidate) => {
      if (candidate.login.imageUrl !== undefined) {
          this.userService.getImageData(candidate.login.id).subscribe((response) => {
            if (response !== undefined) {
              const responseJson = response.body;
              if (responseJson) {
                candidate.login.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
              } else {
                //this.noImage = true;
              }
            }
          });
        }
    });
  }
}
