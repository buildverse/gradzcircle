import { Principal } from '../../core/auth/principal.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CandidateProject } from './candidate-project.model';
import { CandidateProjectService } from './candidate-project.service';

@Component({
    selector: 'jhi-candidate-project',
    templateUrl: './candidate-project.component.html'
})
export class CandidateProjectComponent implements OnInit, OnDestroy {
    candidateProjects: CandidateProject[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private candidateProjectService: CandidateProjectService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.candidateProjectService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<CandidateProject[]>) => (this.candidateProjects = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.candidateProjectService.query().subscribe(
            (res: HttpResponse<CandidateProject[]>) => {
                this.candidateProjects = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCandidateProjects();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CandidateProject) {
        return item.id;
    }
    registerChangeInCandidateProjects() {
        this.eventSubscriber = this.eventManager.subscribe('candidateProjectListModification', response => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
