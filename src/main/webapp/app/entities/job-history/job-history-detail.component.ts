import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobHistory } from './job-history.model';
import { JobHistoryService } from './job-history.service';

@Component({
    selector: 'jhi-job-history-detail',
    templateUrl: './job-history-detail.component.html'
})
export class JobHistoryDetailComponent implements OnInit, OnDestroy {

    jobHistory: JobHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobHistoryService: JobHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobHistories();
    }

    load(id) {
        this.jobHistoryService.find(id).subscribe((jobHistory) => {
            this.jobHistory = jobHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobHistoryListModification',
            (response) => this.load(this.jobHistory.id)
        );
    }
}
