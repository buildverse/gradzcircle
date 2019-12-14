import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { CandidateEmployment } from './candidate-employment.model';
import { CandidateEmploymentService } from './candidate-employment.service';

@Component({
    selector: 'jhi-candidate-employment-detail',
    templateUrl: './candidate-employment-detail.component.html'
})
export class CandidateEmploymentDetailComponent implements OnInit, OnDestroy {
    candidateEmployment: CandidateEmployment;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateEmploymentService: CandidateEmploymentService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateEmployments();
    }

    load(id) {
        this.candidateEmploymentService.find(id).subscribe((candidateEmploymentResponse: HttpResponse<CandidateEmployment>) => {
            this.candidateEmployment = candidateEmploymentResponse.body;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateEmployments() {
        this.eventSubscriber = this.eventManager.subscribe('candidateEmploymentListModification', response =>
            this.load(this.candidateEmployment.id)
        );
    }
}
