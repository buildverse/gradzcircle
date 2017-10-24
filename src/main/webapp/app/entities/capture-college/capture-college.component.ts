import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { CaptureCollege } from './capture-college.model';
import { CaptureCollegeService } from './capture-college.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-capture-college',
    templateUrl: './capture-college.component.html'
})
export class CaptureCollegeComponent implements OnInit, OnDestroy {
captureColleges: CaptureCollege[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private captureCollegeService: CaptureCollegeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.captureCollegeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.captureColleges = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.captureCollegeService.query().subscribe(
            (res: ResponseWrapper) => {
                this.captureColleges = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
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
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCaptureColleges();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CaptureCollege) {
        return item.id;
    }
    registerChangeInCaptureColleges() {
        this.eventSubscriber = this.eventManager.subscribe('captureCollegeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
