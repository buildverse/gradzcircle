import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CandidateSkills } from './candidate-skills.model';
import { CandidateSkillsService } from './candidate-skills.service';

@Component({
    selector: 'jhi-candidate-skills-detail',
    templateUrl: './candidate-skills-detail.component.html'
})
export class CandidateSkillsDetailComponent implements OnInit, OnDestroy {

    candidateSkills: CandidateSkills;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateSkillsService: CandidateSkillsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateSkills();
    }

    load(id) {
        this.candidateSkillsService.find(id)
            .subscribe((candidateSkillsResponse: HttpResponse<CandidateSkills>) => {
                this.candidateSkills = candidateSkillsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateSkills() {
        this.eventSubscriber = this.eventManager.subscribe(
            'candidateSkillsListModification',
            (response) => this.load(this.candidateSkills.id)
        );
    }
}
