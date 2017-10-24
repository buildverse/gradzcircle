import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { CandidateService } from './candidate.service';

@Component({
    selector: 'jhi-candidate-detail',
    templateUrl: './candidate-detail.component.html'
})
export class CandidateDetailComponent implements OnInit, OnDestroy {

    candidate: Candidate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateService: CandidateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCandidates();
    }

    load(id) {
        this.candidateService.find(id).subscribe((candidate) => {
            this.candidate = candidate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'candidateListModification',
            (response) => this.load(this.candidate.id)
        );
    }
}
