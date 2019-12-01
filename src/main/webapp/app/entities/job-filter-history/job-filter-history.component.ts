import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobFilterHistory } from './job-filter-history.model';
import { JobFilterHistoryService } from './job-filter-history.service';

@Component({
    selector: 'jhi-job-filter-history',
    templateUrl: './job-filter-history.component.html'
})
export class JobFilterHistoryComponent implements OnInit, OnDestroy {
    jobFilterHistories: JobFilterHistory[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private jobFilterHistoryService: JobFilterHistoryService,
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
            this.jobFilterHistoryService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<JobFilterHistory[]>) => (this.jobFilterHistories = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.jobFilterHistoryService.query().subscribe(
            (res: HttpResponse<JobFilterHistory[]>) => {
                this.jobFilterHistories = res.body;
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
        this.registerChangeInJobFilterHistories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: JobFilterHistory) {
        return item.id;
    }
    registerChangeInJobFilterHistories() {
        this.eventSubscriber = this.eventManager.subscribe('jobFilterHistoryListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
