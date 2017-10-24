import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusService } from './marital-status.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-marital-status',
    templateUrl: './marital-status.component.html'
})
export class MaritalStatusComponent implements OnInit, OnDestroy {
maritalStatuses: MaritalStatus[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private maritalStatusService: MaritalStatusService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.maritalStatusService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.maritalStatuses = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.maritalStatusService.query().subscribe(
            (res: ResponseWrapper) => {
                this.maritalStatuses = res.json;
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
        this.registerChangeInMaritalStatuses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: MaritalStatus) {
        return item.id;
    }
    registerChangeInMaritalStatuses() {
        this.eventSubscriber = this.eventManager.subscribe('maritalStatusListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
