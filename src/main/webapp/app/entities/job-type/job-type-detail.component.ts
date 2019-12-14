import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { JobType } from './job-type.model';
import { JobTypeService } from './job-type.service';

@Component({
    selector: 'jhi-job-type-detail',
    templateUrl: './job-type-detail.component.html'
})
export class JobTypeDetailComponent implements OnInit, OnDestroy {
    jobType: JobType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager, private jobTypeService: JobTypeService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInJobTypes();
    }

    load(id) {
        this.jobTypeService.find(id).subscribe((jobTypeResponse: HttpResponse<JobType>) => {
            this.jobType = jobTypeResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobTypes() {
        this.eventSubscriber = this.eventManager.subscribe('jobTypeListModification', response => this.load(this.jobType.id));
    }
}
