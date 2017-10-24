import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { EmploymentType } from './employment-type.model';
import { EmploymentTypeService } from './employment-type.service';

@Component({
    selector: 'jhi-employment-type-detail',
    templateUrl: './employment-type-detail.component.html'
})
export class EmploymentTypeDetailComponent implements OnInit, OnDestroy {

    employmentType: EmploymentType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private employmentTypeService: EmploymentTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmploymentTypes();
    }

    load(id) {
        this.employmentTypeService.find(id).subscribe((employmentType) => {
            this.employmentType = employmentType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmploymentTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'employmentTypeListModification',
            (response) => this.load(this.employmentType.id)
        );
    }
}
