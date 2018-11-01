import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ErrorMessages } from './error-messages.model';
import { ErrorMessagesService } from './error-messages.service';
import { Principal } from '../../shared';

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
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.errorMessagesService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<ErrorMessages[]>) => this.errorMessages = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.errorMessagesService.query().subscribe(
            (res: HttpResponse<ErrorMessages[]>) => {
                this.errorMessages = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
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
