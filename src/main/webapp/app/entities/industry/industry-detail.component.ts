import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Industry } from './industry.model';
import { IndustryService } from './industry.service';

@Component({
    selector: 'jhi-industry-detail',
    templateUrl: './industry-detail.component.html'
})
export class IndustryDetailComponent implements OnInit, OnDestroy {
    industry: Industry;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager, private industryService: IndustryService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInIndustries();
    }

    load(id) {
        this.industryService.find(id).subscribe((industryResponse: HttpResponse<Industry>) => {
            this.industry = industryResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIndustries() {
        this.eventSubscriber = this.eventManager.subscribe('industryListModification', response => this.load(this.industry.id));
    }
}
