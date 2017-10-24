import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesService } from './error-messages.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-error-messages',
    templateUrl: './error-messages.component.html'
})
export class ErrorMessagesComponent implements OnInit, OnDestroy {
errorMessages: ErrorMessages[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private errorMessagesService: ErrorMessagesService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.errorMessagesService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.errorMessages = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.errorMessagesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.errorMessages = res.json;
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
        this.registerChangeInErrorMessages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ErrorMessages) {
        return item.id;
    }
    registerChangeInErrorMessages() {
        this.eventSubscriber = this.eventManager.subscribe('errorMessagesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
