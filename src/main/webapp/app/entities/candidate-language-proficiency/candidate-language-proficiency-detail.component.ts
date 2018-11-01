import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateLanguageProficiency } from './candidate-language-proficiency.model';
import { CandidateLanguageProficiencyService } from './candidate-language-proficiency.service';

@Component({
    selector: 'jhi-candidate-language-proficiency-detail',
    templateUrl: './candidate-language-proficiency-detail.component.html'
})
export class CandidateLanguageProficiencyDetailComponent implements OnInit, OnDestroy {

    candidateLanguageProficiency: CandidateLanguageProficiency;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateLanguageProficiencyService: CandidateLanguageProficiencyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateLanguageProficiencies();
    }

    load(id) {
        this.candidateLanguageProficiencyService.find(id)
            .subscribe((candidateLanguageProficiencyResponse: HttpResponse<CandidateLanguageProficiency>) => {
                this.candidateLanguageProficiency = candidateLanguageProficiencyResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateLanguageProficiencies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'candidateLanguageProficiencyListModification',
            (response) => this.load(this.candidateLanguageProficiency.id)
        );
    }
}
