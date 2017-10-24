import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobCategory } from './job-category.model';
import { JobCategoryService } from './job-category.service';

@Component({
    selector: 'jhi-job-category-detail',
    templateUrl: './job-category-detail.component.html'
})
export class JobCategoryDetailComponent implements OnInit, OnDestroy {

    jobCategory: JobCategory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobCategoryService: JobCategoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobCategories();
    }

    load(id) {
        this.jobCategoryService.find(id).subscribe((jobCategory) => {
            this.jobCategory = jobCategory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobCategories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobCategoryListModification',
            (response) => this.load(this.jobCategory.id)
        );
    }
}
