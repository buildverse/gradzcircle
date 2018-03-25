import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Filter } from './filter.model';
import { FilterService } from './filter.service';

@Component({
    selector: 'jhi-filter-detail',
    templateUrl: './filter-detail.component.html'
})
export class FilterDetailComponent implements OnInit, OnDestroy {

    filter: Filter;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private filterService: FilterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFilters();
    }

    load(id) {
        this.filterService.find(id).subscribe((filter) => {
            this.filter = filter;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFilters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'filterListModification',
            (response) => this.load(this.filter.id)
        );
    }
}
