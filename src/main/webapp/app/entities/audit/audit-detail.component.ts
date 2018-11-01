import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Audit } from './audit.model';
import { AuditService } from './audit.service';

@Component({
    selector: 'jhi-audit-detail',
    templateUrl: './audit-detail.component.html'
})
export class AuditDetailComponent implements OnInit, OnDestroy {

    audit: Audit;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private auditService: AuditService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAudits();
    }

    load(id) {
        this.auditService.find(id)
            .subscribe((auditResponse: HttpResponse<Audit>) => {
                this.audit = auditResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAudits() {
        this.eventSubscriber = this.eventManager.subscribe(
            'auditListModification',
            (response) => this.load(this.audit.id)
        );
    }
}
