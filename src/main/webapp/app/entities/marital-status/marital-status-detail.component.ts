import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { MaritalStatus } from './marital-status.model';
import { MaritalStatusService } from './marital-status.service';

@Component({
    selector: 'jhi-marital-status-detail',
    templateUrl: './marital-status-detail.component.html'
})
export class MaritalStatusDetailComponent implements OnInit, OnDestroy {

    maritalStatus: MaritalStatus;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private maritalStatusService: MaritalStatusService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMaritalStatuses();
    }

    load(id) {
        this.maritalStatusService.find(id).subscribe((maritalStatus) => {
            this.maritalStatus = maritalStatus;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMaritalStatuses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'maritalStatusListModification',
            (response) => this.load(this.maritalStatus.id)
        );
    }
}
