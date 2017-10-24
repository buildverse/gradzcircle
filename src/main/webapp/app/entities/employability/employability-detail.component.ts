import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Employability } from './employability.model';
import { EmployabilityService } from './employability.service';

@Component({
    selector: 'jhi-employability-detail',
    templateUrl: './employability-detail.component.html'
})
export class EmployabilityDetailComponent implements OnInit, OnDestroy {

    employability: Employability;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private employabilityService: EmployabilityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmployabilities();
    }

    load(id) {
        this.employabilityService.find(id).subscribe((employability) => {
            this.employability = employability;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmployabilities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'employabilityListModification',
            (response) => this.load(this.employability.id)
        );
    }
}
