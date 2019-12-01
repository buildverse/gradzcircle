import { UserService } from '../../core/user/user.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { JobService } from './job.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { HttpResponse } from '@angular/common/http';
import { CandidateList } from './candidate-list.model';
import { JOB_ID, CORPORATE_ID, CANDIDATE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { JobConstants } from './job.constants';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'jhi-applied-candidate-list',
    templateUrl: './applied-candidate-list.component.html',
    styleUrls: ['job.css']
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
    isMatchScoreCollapsed: boolean;
    matchScoreFrom?: number;
    matchScoreTo?: number;
    matchScoreRange?: string;
    defaultImage = require('../../../content/images/no-image.png');

    constructor(
        private jobService: JobService,
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private dataStorageService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private userService: UserService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.isMatchScoreCollapsed = true;
        // this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
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

    reset() {
        this.page = 0;
        this.candidateList = [];
        if (this.jobId) {
            this.loadAppliedCandidates();
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/appliedCandidateList/'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                // search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });

        this.loadAppliedCandidates();
    }

    loadAppliedCandidates() {
        this.setMatchScoreRange();
        this.spinnerService.show();
        this.jobService
            .queryAppliedCandidatesForJob(
                {
                    page: this.page - 1,
                    //query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    id: this.jobId,
                    score: this.score
                },
                this.matchScoreFrom,
                this.matchScoreTo
            )
            .subscribe(
                (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    ngOnInit() {
        this.jobId = +this.dataStorageService.getData(JOB_ID);
        this.loadAppliedCandidates();
    }

    setPublicProfileRouteParams(candidateId) {
        this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
        this.dataStorageService.setdata(JOB_ID, this.jobId);
        // this.dataStorageService.setdata(CORPORATE_ID, corporateId);
    }

    setJobListRouteParamsForLinkedCandidates(candidateId) {
        this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
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

    clearMatchScores() {
        this.matchScoreRange = undefined;
        this.loadAppliedCandidates();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onError(error) {
        //console.log(error);
        this.spinnerService.hide();
        this.jhiAlertService.error(error.message, null, null);
    }

    private onSuccess(data, headers) {
        // console.log('HEADER IS ----> ' + JSON.stringify(headers));
        // console.log('DATA IS ----> ' + JSON.stringify(data));
        this.spinnerService.hide();
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.candidateList = data;
        this.setImageUrl();
    }

    private setImageUrl() {
        this.candidateList.forEach(candidate => {
            if (candidate.login.imageUrl !== undefined) {
                this.userService.getImageData(candidate.login.id).subscribe(response => {
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
