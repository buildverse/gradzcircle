import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { JobFilterHistory } from './job-filter-history.model';
import { JobFilterHistoryService } from './job-filter-history.service';

@Component({
    selector: 'jhi-job-filter-history-detail',
    templateUrl: './job-filter-history-detail.component.html'
})
export class JobFilterHistoryDetailComponent implements OnInit, OnDestroy {

    jobFilterHistory: JobFilterHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobFilterHistoryService: JobFilterHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobFilterHistories();
    }

    load(id) {
        this.jobFilterHistoryService.find(id)
            .subscribe((jobFilterHistoryResponse: HttpResponse<JobFilterHistory>) => {
                this.jobFilterHistory = jobFilterHistoryResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobFilterHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobFilterHistoryListModification',
            (response) => this.load(this.jobFilterHistory.id)
        );
    }
}
