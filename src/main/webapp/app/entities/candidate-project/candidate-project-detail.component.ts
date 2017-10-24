import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';
import { EditorProperties } from '../../shared';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';

@Component({
    selector: 'jhi-candidate-project-detail',
    templateUrl: './candidate-project-detail.component.html'
})
export class CandidateProjectDetailComponent implements OnInit, OnDestroy {

    candidateProject: CandidateProject;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private candidateProjectService: CandidateProjectService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCandidateProjects();
    }

    load(id) {
        this.candidateProjectService.find(id).subscribe((candidateProject) => {
            this.candidateProject = candidateProject;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCandidateProjects() {
        this.eventSubscriber = this.eventManager.subscribe(
            'candidateProjectListModification',
            (response) => this.load(this.candidateProject.id)
        );
    }
}
