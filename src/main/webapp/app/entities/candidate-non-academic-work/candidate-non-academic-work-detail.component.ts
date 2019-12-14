import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { CandidateNonAcademicWork } from './candidate-non-academic-work.model';
import { CandidateNonAcademicWorkService } from './candidate-non-academic-work.service';

@Component({
    selector: 'jhi-candidate-non-academic-work-detail',
    templateUrl: './candidate-non-academic-work-detail.component.html'
})
export class CandidateNonAcademicWorkDetailComponent implements OnInit, OnDestroy {
    candidateNonAcademicWork: CandidateNonAcademicWork;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateNonAcademicWorkService: CandidateNonAcademicWorkService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateNonAcademicWorks();
    }

    load(id) {
        this.candidateNonAcademicWorkService
            .find(id)
            .subscribe((candidateNonAcademicWorkResponse: HttpResponse<CandidateNonAcademicWork>) => {
                this.candidateNonAcademicWork = candidateNonAcademicWorkResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateNonAcademicWorks() {
        this.eventSubscriber = this.eventManager.subscribe('candidateNonAcademicWorkListModification', response =>
            this.load(this.candidateNonAcademicWork.id)
        );
    }
}
