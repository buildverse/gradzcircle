import { UserService } from '../../core/user/user.service';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { FROM_LINKED_CANDIDATE, CANDIDATE_ID, JOB_ID, CORPORATE_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { CandidateList } from '../job/candidate-list.model';
import { CorporateService } from './corporate.service';
import { HttpResponse } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import { AmazonConstants } from '../../shared/amazon.constants';

@Component({
    selector: 'jhi-linked-candidate-list',
    templateUrl: './corporate-linked-candidates.component.html',
    styleUrls: ['corporate.css']
})
export class LinkedCandidatesComponent implements OnInit, OnDestroy {
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
    defaultImage = AmazonConstants.S3_URL + 'no-image.png';
    userProfile: string;

    constructor(
        private corporateService: CorporateService,
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private dataStorageService: DataStorageService,
        private spinnerService: NgxSpinnerService,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.userProfile = 'corporate';
        // this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    reset() {
        this.page = 0;
        this.candidateList = [];
        if (this.corporateId) {
            this.loadLinkedCandidates();
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/corporate/linkedCandidatesForCorporate'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });

        this.loadLinkedCandidates();
    }

    setViewPublicProfileRouteParams(candidateId, corporateId, fromLinkedCandidate) {
        this.dataStorageService.setdata(CANDIDATE_ID, candidateId);
        this.dataStorageService.setdata(JOB_ID, 0);
        this.dataStorageService.setdata(CORPORATE_ID, this.corporateId);
        this.dataStorageService.setdata(FROM_LINKED_CANDIDATE, fromLinkedCandidate);
    }

    loadLinkedCandidates() {
        this.spinnerService.show();
        this.corporateService
            .queryLinkedCandidates({
                page: this.page - 1,
                size: this.itemsPerPage,
                id: this.corporateId
            })
            .subscribe(
                (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    ngOnInit() {
        this.subscription = this.activatedRoute.params.subscribe(params => {
            if (params['id']) {
                this.corporateId = params['id'];
            } else {
                this.corporateId = this.dataStorageService.getData(CORPORATE_ID);
            }
            this.loadLinkedCandidates();
        });
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    trackId(index: number, item: CandidateList) {
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
        this.spinnerService.hide();
        this.jhiAlertService.error(error.message, null, null);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.candidateList = data;
        this.spinnerService.hide();
    }
}
