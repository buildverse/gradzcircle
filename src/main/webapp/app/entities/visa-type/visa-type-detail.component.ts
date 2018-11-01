import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { VisaType } from './visa-type.model';
import { VisaTypeService } from './visa-type.service';

@Component({
    selector: 'jhi-visa-type-detail',
    templateUrl: './visa-type-detail.component.html'
})
export class VisaTypeDetailComponent implements OnInit, OnDestroy {

    visaType: VisaType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private visaTypeService: VisaTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVisaTypes();
    }

    load(id) {
        this.visaTypeService.find(id)
            .subscribe((visaTypeResponse: HttpResponse<VisaType>) => {
                this.visaType = visaTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVisaTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'visaTypeListModification',
            (response) => this.load(this.visaType.id)
        );
    }
}
