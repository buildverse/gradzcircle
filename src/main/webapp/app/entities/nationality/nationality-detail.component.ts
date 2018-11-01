import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Nationality } from './nationality.model';
import { NationalityService } from './nationality.service';

@Component({
    selector: 'jhi-nationality-detail',
    templateUrl: './nationality-detail.component.html'
})
export class NationalityDetailComponent implements OnInit, OnDestroy {

    nationality: Nationality;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private nationalityService: NationalityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInNationalities();
    }

    load(id) {
        this.nationalityService.find(id)
            .subscribe((nationalityResponse: HttpResponse<Nationality>) => {
                this.nationality = nationalityResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInNationalities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'nationalityListModification',
            (response) => this.load(this.nationality.id)
        );
    }
}
