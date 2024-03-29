import { Principal } from '../../core/auth/principal.service';
import { UserService } from '../../core/user/user.service';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { CANDIDATE_ID, CORPORATE_ID, JOB_ID } from '../../shared/constants/storage.constants';
import { DataStorageService } from '../../shared/helper/localstorage.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Candidate } from './candidate.model';
import { CandidateService } from './candidate.service';
import { CandidateList } from '../job/candidate-list.model';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { AmazonConstants } from '../../shared/amazon.constants';

@Component({
    selector: 'jhi-candidate',
    templateUrl: './candidate-preview.component.html',
    styleUrls: ['candidate.css']
})
export class CandidatePreviewComponent implements OnInit, OnDestroy {
    candidateList: CandidateList[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    defaultImage = AmazonConstants.S3_URL + 'no-image.png';
    profile: string;

    constructor(
        private candidateService: CandidateService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private spinnerService: NgxSpinnerService,
        private router: Router,
        private userService: UserService,
        private dataService: DataStorageService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
        this.profile = 'preview';
    }

    loadAll() {
        this.spinnerService.show();
        this.candidateService
            .queryForGuest({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<CandidateList[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.candidateList = [];
        this.loadAll();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/candidatePreview'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });

        this.loadAll();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCandidates();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Candidate) {
        return item.id;
    }
    registerChangeInCandidates() {
        this.eventSubscriber = this.eventManager.subscribe('candidateListModification', response => this.loadAll());
    }

    private onError(error) {
        this.router.navigate(['/error']);
        this.jhiAlertService.error(error.message, null, null);
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.candidateList = data;
        this.spinnerService.hide();
    }

    setViewPublicProfileRouteParams(candidateId, jobId) {
        this.dataService.setdata(CANDIDATE_ID, candidateId);
        this.dataService.setdata(JOB_ID, -1);
    }
}
