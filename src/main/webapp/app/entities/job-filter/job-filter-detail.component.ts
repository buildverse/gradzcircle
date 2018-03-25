import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobFilter } from './job-filter.model';
import { JobFilterService } from './job-filter.service';

@Component({
    selector: 'jhi-job-filter-detail',
    templateUrl: './job-filter-detail.component.html'
})
export class JobFilterDetailComponent implements OnInit, OnDestroy {

    jobFilter: JobFilter;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobFilterService: JobFilterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobFilters();
    }

    load(id) {
        this.jobFilterService.find(id).subscribe((jobFilter) => {
            this.jobFilter = jobFilter;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobFilters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobFilterListModification',
            (response) => this.load(this.jobFilter.id)
        );
    }
}
