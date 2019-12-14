import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureUniversity } from './capture-university.model';
import { CaptureUniversityService } from './capture-university.service';

@Component({
    selector: 'jhi-capture-university-detail',
    templateUrl: './capture-university-detail.component.html'
})
export class CaptureUniversityDetailComponent implements OnInit, OnDestroy {
    captureUniversity: CaptureUniversity;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private captureUniversityService: CaptureUniversityService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInCaptureUniversities();
    }

    load(id) {
        this.captureUniversityService.find(id).subscribe((captureUniversityResponse: HttpResponse<CaptureUniversity>) => {
            this.captureUniversity = captureUniversityResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCaptureUniversities() {
        this.eventSubscriber = this.eventManager.subscribe('captureUniversityListModification', response =>
            this.load(this.captureUniversity.id)
        );
    }
}
