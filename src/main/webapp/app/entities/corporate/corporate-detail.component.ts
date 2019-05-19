import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { Corporate } from './corporate.model';
import { CorporateService } from './corporate.service';

@Component({
    selector: 'jhi-corporate-detail',
    templateUrl: './corporate-detail.component.html'
})
export class CorporateDetailComponent implements OnInit, OnDestroy {

    corporate: Corporate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private corporateService: CorporateService,
        private route: ActivatedRoute
       
    ) {
    }

    ngOnInit() {
      this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
   
        this.registerChangeInCorporates();
    }

    load(id) {
         this.corporateService.find(id)
            .subscribe((corporateResponse: HttpResponse<Corporate>) => {
                this.corporate = corporateResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCorporates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'corporateListModification',
            (response) => this.load(this.corporate.id)
        );
    }
}
