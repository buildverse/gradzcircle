import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { CandidateEducation } from './candidate-education.model';
import { CandidateEducationService } from './candidate-education.service';

@Component({
    selector: 'jhi-candidate-education-detail',
    templateUrl: './candidate-education-detail.component.html'
})
export class CandidateEducationDetailComponent implements OnInit, OnDestroy {
    candidateEducation: CandidateEducation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateEducationService: CandidateEducationService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateEducations();
    }

    load(id) {
        this.candidateEducationService.find(id).subscribe((candidateEducationResponse: HttpResponse<CandidateEducation>) => {
            this.candidateEducation = candidateEducationResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateEducations() {
        this.eventSubscriber = this.eventManager.subscribe('candidateEducationListModification', response =>
            this.load(this.candidateEducation.id)
        );
    }
}
