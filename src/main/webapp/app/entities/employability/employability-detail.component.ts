import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Employability } from './employability.model';
import { EmployabilityService } from './employability.service';

@Component({
    selector: 'jhi-employability-detail',
    templateUrl: './employability-detail.component.html'
})
export class EmployabilityDetailComponent implements OnInit, OnDestroy {
    employability: Employability;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager, private employabilityService: EmployabilityService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInEmployabilities();
    }

    load(id) {
        this.employabilityService.find(id).subscribe((employabilityResponse: HttpResponse<Employability>) => {
            this.employability = employabilityResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmployabilities() {
        this.eventSubscriber = this.eventManager.subscribe('employabilityListModification', response => this.load(this.employability.id));
    }
}
