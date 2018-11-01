import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { University } from './university.model';
import { UniversityService } from './university.service';

@Component({
    selector: 'jhi-university-detail',
    templateUrl: './university-detail.component.html'
})
export class UniversityDetailComponent implements OnInit, OnDestroy {

    university: University;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private universityService: UniversityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUniversities();
    }

    load(id) {
        this.universityService.find(id)
            .subscribe((universityResponse: HttpResponse<University>) => {
                this.university = universityResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUniversities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'universityListModification',
            (response) => this.load(this.university.id)
        );
    }
}
