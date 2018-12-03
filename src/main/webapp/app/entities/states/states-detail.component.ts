import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { States } from './states.model';
import { StatesService } from './states.service';

@Component({
    selector: 'jhi-states-detail',
    templateUrl: './states-detail.component.html'
})
export class StatesDetailComponent implements OnInit, OnDestroy {

    states: States;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private statesService: StatesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStates();
    }

    load(id) {
        this.statesService.find(id)
            .subscribe((statesResponse: HttpResponse<States>) => {
                this.states = statesResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'statesListModification',
            (response) => this.load(this.states.id)
        );
    }
}
