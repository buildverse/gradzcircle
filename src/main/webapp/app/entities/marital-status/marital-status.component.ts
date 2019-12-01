import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusService } from './marital-status.service';

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
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.maritalStatusService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<MaritalStatus[]>) => (this.maritalStatuses = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.maritalStatusService.query().subscribe(
            (res: HttpResponse<MaritalStatus[]>) => {
                this.maritalStatuses = res.body;
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
        this.principal.identity().then(account => {
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
        this.eventSubscriber = this.eventManager.subscribe('maritalStatusListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
