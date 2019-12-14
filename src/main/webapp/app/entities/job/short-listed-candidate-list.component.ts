import { UserService } from '../../core/user/user.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { JobService } from './job.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { CandidateList } from './candidate-list.model';
import { HttpResponse } from '@angular/common/http';
import { JOB_ID, CORPORATE_ID, CANDIDATE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';

@Component({
    selector: 'jhi-short-listed-candidate-list',
    templateUrl: './short-listed-candidate-list.html',
    styleUrls: ['job.css']
})
export class ShortListedCandidateListComponent implements OnInit, OnDestroy {
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

    constructor(
        private jobService: JobService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private dataStorageService: DataStorageService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
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
            this.loadShortListedCandidates();
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/shortListedCandidateList/'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                // search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });

        this.loadShortListedCandidates();
    }

    loadShortListedCandidates() {
        this.jobService
            .queryShortListedCandidatesForJob({
                page: this.page - 1,
                // query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                id: this.jobId
            })
            .subscribe(
                (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    ngOnInit() {
        this.jobId = +this.dataStorageService.getData(JOB_ID);
        this.corporateId = +this.dataStorageService.getData(CORPORATE_ID);
        this.loadShortListedCandidates();
        this.registerChangeInShortListed();
    }

    setPublicProfileRouteParams(candidateId, jobId, corporateId) {
        this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
        this.dataStorageService.setdata(JOB_ID, this.jobId);
        this.dataStorageService.setdata(CORPORATE_ID, corporateId);
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    trackId(index: number, item: CandidateList) {
        return item.id;
    }

    registerChangeInShortListed() {
        this.eventSubscriber = this.eventManager.subscribe('shortListedCandidateListModification', response =>
            this.loadShortListedCandidates()
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
        this.candidateList.forEach(candidate => {
            if (candidate.login.imageUrl !== undefined) {
                this.userService.getImageData(candidate.login.id).subscribe(response => {
                    if (response !== undefined) {
                        const responseJson = response.body;
                        if (responseJson) {
                            candidate.login.imageUrl = responseJson[0].href + '?t=' + Math.random().toString();
                        } else {
                            // this.noImage = true;
                        }
                    }
                });
            }
        });
    }
}
