import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { CandidateCertification } from './candidate-certification.model';
import { CandidateCertificationService } from './candidate-certification.service';

@Component({
    selector: 'jhi-candidate-certification-detail',
    templateUrl: './candidate-certification-detail.component.html'
})
export class CandidateCertificationDetailComponent implements OnInit, OnDestroy {

    candidateCertification: CandidateCertification;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateCertificationService: CandidateCertificationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateCertifications();
    }

    load(id) {
        this.candidateCertificationService.find(id).subscribe((candidateCertification) => {
            this.candidateCertification = candidateCertification;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateCertifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'candidateCertificationListModification',
            (response) => this.load(this.candidateCertification.id)
        );
    }
}
