import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { College } from './college.model';
import { CollegeService } from './college.service';

@Component({
    selector: 'jhi-college-detail',
    templateUrl: './college-detail.component.html'
})
export class CollegeDetailComponent implements OnInit, OnDestroy {

    college: College;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private collegeService: CollegeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInColleges();
    }

    load(id) {
        this.collegeService.find(id)
            .subscribe((collegeResponse: HttpResponse<College>) => {
                this.college = collegeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInColleges() {
        this.eventSubscriber = this.eventManager.subscribe(
            'collegeListModification',
            (response) => this.load(this.college.id)
        );
    }
}
