import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiAlertService} from 'ng-jhipster';
import {JobService} from './job.service';
import {ITEMS_PER_PAGE, UserService, DataStorageService} from '../../shared';
import {JOB_ID, CORPORATE_ID, CANDIDATE_ID} from '../../shared/constants/storage.constants';
import {CandidateList} from './candidate-list.model';
import {JobConstants} from './job.constants';
import {HttpResponse} from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'jhi-matched-candidate-list',
  templateUrl: './matched-candidate-list.html',
  styleUrls: ['job.css']
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
  corporateId: number;
  eventSubscriber: Subscription;
  subscription: Subscription;
  defaultImage = require('../../../content/images/no-image.png');
  isMatchScoreCollapsed: boolean;
  matchScoreFrom?: number;
  matchScoreTo?: number;
  matchScoreRange?: string;
  reviewed?: boolean;

  constructor(
    private jobService: JobService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private parseLinks: JhiParseLinks,
    private router: Router,
    private dataStorageService: DataStorageService,
    private spinnerService : NgxSpinnerService

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
    // this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.isMatchScoreCollapsed = true;
    this.reviewed = false;
  }

  setMatchScoreRange() {
    if (this.matchScoreRange === 'matchScore90To100') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_90;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_100;
    } else if (this.matchScoreRange === 'matchScore80To89') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_80;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_89;
    } else if (this.matchScoreRange === 'matchScore70To79') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_70;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_79;
    } else if (this.matchScoreRange === 'matchScore60To69') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_60;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_69;
    } else if (this.matchScoreRange === 'matchScore50To59') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_50;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_59;
    } else if (this.matchScoreRange === 'matchScorelessThan50') {
      this.matchScoreFrom = JobConstants.MATCH_SCORE_EQUAL_TO_0;
      this.matchScoreTo = JobConstants.MATCH_SCORE_EQUAL_TO_50;
    } else {
      this.matchScoreFrom = -1;
      this.matchScoreTo = -1;
    }
  }


  toggleReviewedFilter(event:any){
    this.reviewed = event;
    this.loadMatchedCandidates();
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
      //  sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });

    this.loadMatchedCandidates();
  }

  loadMatchedCandidates() {
    this.setMatchScoreRange();
    this.spinnerService.show();
    this.jobService.queryMatchedCandidatesForJob({
      page: this.page - 1,
      //query: this.currentSearch,
      size: this.itemsPerPage,
    //  sort: this.sort(),
      id: this.jobId,
    }, this.matchScoreFrom, this.matchScoreTo,this.reviewed).subscribe(
      (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
      (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  clearMatchScores() {
    this.matchScoreRange = undefined;
    this.loadMatchedCandidates();
  }

  ngOnInit() {
    this.matchScoreFrom = -1;
    this.matchScoreTo = -1;
    this.matchScoreRange = undefined;
    this.subscription = this.activatedRoute.params.subscribe((params) => {

      if (params['id'] && params['corporateId']) {
        this.jobId = params['id'];
        this.corporateId = params['corporateId'];
      } else {
        this.jobId = parseFloat(this.dataStorageService.getData(JOB_ID));
        this.corporateId = parseFloat(this.dataStorageService.getData(CORPORATE_ID));
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
    this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
    this.dataStorageService.setdata(JOB_ID, jobId);
    this.dataStorageService.setdata(CORPORATE_ID, corporateId);
  }
  
  setJobListRouteParamsForLinkedCandidates(candidateId,corporateId) {
    this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
    this.dataStorageService.setdata(CORPORATE_ID, corporateId);
  }

  ngOnDestroy() {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    this.jhiAlertService.clear();
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
    this.candidateList = data;
    if (this.candidateList && this.candidateList.length > 0){
      if (!this.candidateList[0].canBuy) {
        this.jhiAlertService.addAlert({type: 'danger', msg: 'gradzcircleApp.job.topUpAlert'}, []);
      }
    }
    this.setImageUrl();
   
  }

  private setImageUrl() {
    if (this.candidateList.length == 0) {
      this.spinnerService.hide();
    } else {
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
        this.spinnerService.hide();
      });
    }
  }
}
