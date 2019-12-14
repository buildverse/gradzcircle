import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CaptureCollege } from './capture-college.model';
import { CaptureCollegeService } from './capture-college.service';

@Component({
    selector: 'jhi-capture-college-detail',
    templateUrl: './capture-college-detail.component.html'
})
export class CaptureCollegeDetailComponent implements OnInit, OnDestroy {
    captureCollege: CaptureCollege;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private captureCollegeService: CaptureCollegeService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInCaptureColleges();
    }

    load(id) {
        this.captureCollegeService.find(id).subscribe((captureCollegeResponse: HttpResponse<CaptureCollege>) => {
            this.captureCollege = captureCollegeResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCaptureColleges() {
        this.eventSubscriber = this.eventManager.subscribe('captureCollegeListModification', response => this.load(this.captureCollege.id));
    }
}
