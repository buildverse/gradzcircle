import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureQualification } from './capture-qualification.model';
import { CaptureQualificationService } from './capture-qualification.service';

@Component({
    selector: 'jhi-capture-qualification-detail',
    templateUrl: './capture-qualification-detail.component.html'
})
export class CaptureQualificationDetailComponent implements OnInit, OnDestroy {

    captureQualification: CaptureQualification;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private captureQualificationService: CaptureQualificationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCaptureQualifications();
    }

    load(id) {
        this.captureQualificationService.find(id).subscribe((captureQualification) => {
            this.captureQualification = captureQualification;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCaptureQualifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'captureQualificationListModification',
            (response) => this.load(this.captureQualification.id)
        );
    }
}
